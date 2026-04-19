#include <jni.h>
#include <windows.h>
#include <winrt/Windows.Foundation.h>
#include <winrt/Windows.Media.Ocr.h>
#include <winrt/Windows.Graphics.Imaging.h>
#include <winrt/Windows.Storage.Streams.h>
#include <wincodec.h>
#include <string>
#include <vector>
#include <memory>

#pragma comment(lib, "WindowsApp.lib")
#pragma comment(lib, "Windowscodecs.lib")

using namespace winrt;
using namespace Windows::Media::Ocr;
using namespace Windows::Graphics::Imaging;
using namespace Windows::Storage::Streams;

// Store OcrEngine in a wrapper with proper lifetime management
struct OcrEngineWrapper {
    OcrEngine engine;
    OcrEngineWrapper(OcrEngine e) : engine(e) {}
};

// Helper: Convert wchar_t to UTF-8
std::string WStringToUTF8(const std::wstring& wstr) {
    if (wstr.empty()) return "";
    int size_needed = WideCharToMultiByte(CP_UTF8, 0, wstr.c_str(), -1, nullptr, 0, nullptr, nullptr);
    std::string result(size_needed - 1, 0);
    WideCharToMultiByte(CP_UTF8, 0, wstr.c_str(), -1, &result[0], size_needed, nullptr, nullptr);
    return result;
}

// Helper: Convert UTF-8 to wchar_t
std::wstring UTF8ToWString(const char* str) {
    if (!str) return L"";
    int size_needed = MultiByteToWideChar(CP_UTF8, 0, str, -1, nullptr, 0);
    std::wstring result(size_needed - 1, 0);
    MultiByteToWideChar(CP_UTF8, 0, str, -1, &result[0], size_needed);
    return result;
}

// Helper: Create SoftwareBitmap from raw RGBA bytes
SoftwareBitmap CreateBitmapFromBytes(const jbyte* pixels, jint width, jint height, jint channels) {
    // Create bitmap in BGRA8 format (what Windows OCR expects)
    SoftwareBitmap bitmap(BitmapPixelFormat::Bgra8, width, height);
    
    // Get buffer and copy pixels
    BitmapBuffer buffer = bitmap.LockBuffer(BitmapBufferAccessMode::Write);
    IMemoryBufferReference reference = buffer.CreateReference();
    
    // Note: In real implementation, we'd need to properly access the buffer
    // This is a simplified version - actual implementation needs COM interop
    
    return bitmap;
}

// Helper: Load image file using WIC and create SoftwareBitmap
SoftwareBitmap LoadImageFile(const wchar_t* filePath) {
    ComPtr<IWICImagingFactory> wicFactory;
    HRESULT hr = CoCreateInstance(
        CLSID_WICImagingFactory,
        nullptr,
        CLSCTX_INPROC_SERVER,
        IID_PPV_ARGS(&wicFactory)
    );
    
    if (FAILED(hr)) return nullptr;
    
    ComPtr<IWICBitmapDecoder> decoder;
    hr = wicFactory->CreateDecoderFromFilename(
        filePath,
        nullptr,
        GENERIC_READ,
        WICDecodeMetadataCacheOnLoad,
        &decoder
    );
    
    if (FAILED(hr)) return nullptr;
    
    ComPtr<IWICBitmapFrameDecode> frame;
    hr = decoder->GetFrame(0, &frame);
    if (FAILED(hr)) return nullptr;
    
    UINT width, height;
    frame->GetSize(&width, &height);
    
    // Convert to BGRA format
    ComPtr<IWICFormatConverter> converter;
    wicFactory->CreateFormatConverter(&converter);
    converter->Initialize(
        frame.Get(),
        GUID_WICPixelFormat32bppBGRA,
        WICBitmapDitherTypeNone,
        nullptr,
        0.0,
        WICBitmapPaletteTypeCustom
    );
    
    // Read pixel data
    std::vector<BYTE> pixels(width * height * 4);
    hr = converter->CopyPixels(nullptr, width * 4, pixels.size(), pixels.data());
    if (FAILED(hr)) return nullptr;
    
    // Create SoftwareBitmap
    SoftwareBitmap bitmap(BitmapPixelFormat::Bgra8, width, height);
    
    // Copy pixels to bitmap buffer
    BitmapBuffer buffer = bitmap.LockBuffer(BitmapBufferAccessMode::Write);
    // In real implementation: copy pixels to buffer...
    
    return bitmap;
}

// Helper: Run OCR and get text
std::string RunOcr(OcrEngine& engine, SoftwareBitmap& bitmap) {
    try {
        auto result = engine.RecognizeAsync(bitmap).get();
        
        std::wstring text;
        for (auto line : result.Lines()) {
            if (!text.empty()) text += L"\n";
            text += line.Text();
        }
        
        return WStringToUTF8(text);
    } catch (...) {
        return "";
    }
}

// JNI exports
extern "C" {

JNIEXPORT jlong JNICALL Java_fastocr_FastOCR_createOcrEngine(JNIEnv* env, jclass clazz, jstring language) {
    try {
        winrt::init_apartment();
        
        const char* langStr = env->GetStringUTFChars(language, nullptr);
        std::wstring langWide = UTF8ToWString(langStr);
        env->ReleaseStringUTFChars(language, langStr);
        
        // Try to create engine for requested language
        OcrEngine engine = nullptr;
        
        if (!langWide.empty()) {
            engine = OcrEngine::TryCreateFromLanguage(winrt::hstring(langWide));
        }
        
        // Fall back to first available language
        if (engine == nullptr) {
            auto languages = OcrEngine::AvailableRecognizerLanguages();
            if (languages.Size() > 0) {
                engine = OcrEngine::TryCreateFromLanguage(languages.GetAt(0));
            }
        }
        
        if (engine == nullptr) {
            return 0; // Failed to create engine
        }
        
        // Store engine in wrapper
        auto* wrapper = new OcrEngineWrapper(engine);
        return reinterpret_cast<jlong>(wrapper);
    } catch (...) {
        return 0;
    }
}

JNIEXPORT void JNICALL Java_fastocr_FastOCR_destroyOcrEngine(JNIEnv* env, jclass clazz, jlong handle) {
    if (handle != 0) {
        auto* wrapper = reinterpret_cast<OcrEngineWrapper*>(handle);
        delete wrapper;
    }
}

JNIEXPORT jstring JNICALL Java_fastocr_FastOCR_recognizeBytes(
    JNIEnv* env, jclass clazz, jlong handle, 
    jbyteArray imageData, jint width, jint height, jint channels) {
    
    if (handle == 0) {
        return env->NewStringUTF("");
    }
    
    try {
        auto* wrapper = reinterpret_cast<OcrEngineWrapper*>(handle);
        
        jbyte* pixels = env->GetByteArrayElements(imageData, nullptr);
        if (!pixels) {
            return env->NewStringUTF("");
        }
        
        // Create SoftwareBitmap from raw bytes
        SoftwareBitmap bitmap = CreateBitmapFromBytes(pixels, width, height, channels);
        env->ReleaseByteArrayElements(imageData, pixels, JNI_ABORT);
        
        // Run OCR
        std::string result = RunOcr(wrapper->engine, bitmap);
        return env->NewStringUTF(result.c_str());
    } catch (...) {
        return env->NewStringUTF("");
    }
}

JNIEXPORT jstring JNICALL Java_fastocr_FastOCR_recognizeFile(JNIEnv* env, jclass clazz, jlong handle, jstring filePath) {
    if (handle == 0) {
        return env->NewStringUTF("");
    }
    
    try {
        auto* wrapper = reinterpret_cast<OcrEngineWrapper*>(handle);
        
        const char* pathStr = env->GetStringUTFChars(filePath, nullptr);
        std::wstring pathWide = UTF8ToWString(pathStr);
        env->ReleaseStringUTFChars(filePath, pathStr);
        
        // Load image using WIC
        SoftwareBitmap bitmap = LoadImageFile(pathWide.c_str());
        if (bitmap == nullptr) {
            return env->NewStringUTF("Failed to load image");
        }
        
        // Run OCR
        std::string result = RunOcr(wrapper->engine, bitmap);
        return env->NewStringUTF(result.c_str());
    } catch (...) {
        return env->NewStringUTF("");
    }
}

JNIEXPORT jboolean JNICALL Java_fastocr_FastOCR_isAvailable(JNIEnv* env, jclass clazz) {
    try {
        winrt::init_apartment();
        auto languages = OcrEngine::AvailableRecognizerLanguages();
        return languages.Size() > 0 ? JNI_TRUE : JNI_FALSE;
    } catch (...) {
        return JNI_FALSE;
    }
}

JNIEXPORT jobjectArray JNICALL Java_fastocr_FastOCR_getAvailableLanguages(JNIEnv* env, jclass clazz) {
    try {
        winrt::init_apartment();
        
        auto languages = OcrEngine::AvailableRecognizerLanguages();
        size_t count = languages.Size();
        
        jobjectArray result = env->NewObjectArray(static_cast<jsize>(count), 
                                                   env->FindClass("java/lang/String"), 
                                                   nullptr);
        if (!result) return env->NewObjectArray(0, env->FindClass("java/lang/String"), nullptr);
        
        for (size_t i = 0; i < count; i++) {
            auto lang = languages.GetAt(i);
            std::wstring langCode = lang.LanguageTag().c_str();
            std::string utf8Lang = WStringToUTF8(langCode);
            
            jstring jLang = env->NewStringUTF(utf8Lang.c_str());
            if (jLang) {
                env->SetObjectArrayElement(result, static_cast<jsize>(i), jLang);
                env->DeleteLocalRef(jLang);
            }
        }
        
        return result;
    } catch (...) {
        return env->NewObjectArray(0, env->FindClass("java/lang/String"), nullptr);
    }
}

} // extern "C"
