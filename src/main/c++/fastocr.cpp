#include <jni.h>
#include <windows.h>
#include <winrt/Windows.Foundation.h>
#include <winrt/Windows.Media.Ocr.h>
#include <winrt/Windows.Graphics.Imaging.h>
#include <winrt/Windows.Storage.Streams.h>
#include <string>
#include <vector>

using namespace winrt;
using namespace Windows::Media::Ocr;
using namespace Windows::Graphics::Imaging;
using namespace Windows::Storage::Streams;

// JNI exports
extern "C" {

JNIEXPORT jlong JNICALL Java_fastocr_FastOCR_createOcrEngine(JNIEnv* env, jclass clazz, jstring language) {
    try {
        init_apartment();
        
        const char* langStr = env->GetStringUTFChars(language, nullptr);
        std::wstring langWide(langStr, langStr + strlen(langStr));
        env->ReleaseStringUTFChars(language, langStr);
        
        // Get OCR engine for language
        auto engine = OcrEngine::TryCreateFromLanguage(winrt::hstring(langWide));
        
        if (engine == nullptr) {
            // Fall back to default
            engine = OcrEngine::TryCreateFromLanguage(OcrEngine::AvailableRecognizerLanguages().GetAt(0));
        }
        
        // Store engine pointer (simplified - real impl needs proper memory management)
        return reinterpret_cast<jlong>(&engine);
    } catch (...) {
        return 0;
    }
}

JNIEXPORT void JNICALL Java_fastocr_FastOCR_destroyOcrEngine(JNIEnv* env, jclass clazz, jlong handle) {
    // Cleanup would go here
}

JNIEXPORT jstring JNICALL Java_fastocr_FastOCR_recognizeBytes(
    JNIEnv* env, jclass clazz, jlong handle, 
    jbyteArray imageData, jint width, jint height, jint channels) {
    
    try {
        jbyte* pixels = env->GetByteArrayElements(imageData, nullptr);
        
        // Create SoftwareBitmap from raw bytes
        // This is a simplified version - real implementation needs proper WinRT buffer handling
        
        // For now, return placeholder
        std::string result = "OCR from bytes not yet implemented in JNI stub";
        
        env->ReleaseByteArrayElements(imageData, pixels, JNI_ABORT);
        
        return env->NewStringUTF(result.c_str());
    } catch (...) {
        return env->NewStringUTF("");
    }
}

JNIEXPORT jstring JNICALL Java_fastocr_FastOCR_recognizeFile(JNIEnv* env, jclass clazz, jlong handle, jstring filePath) {
    try {
        const char* pathStr = env->GetStringUTFChars(filePath, nullptr);
        std::wstring pathWide(pathStr, pathStr + strlen(pathStr));
        env->ReleaseStringUTFChars(filePath, pathStr);
        
        // This is a stub - real implementation would:
        // 1. Load image file using Windows Imaging Component
        // 2. Create SoftwareBitmap
        // 3. Run OCR engine
        // 4. Return concatenated text from lines
        
        std::string result = "File OCR not yet implemented in JNI stub";
        return env->NewStringUTF(result.c_str());
    } catch (...) {
        return env->NewStringUTF("");
    }
}

JNIEXPORT jboolean JNICALL Java_fastocr_FastOCR_isAvailable(JNIEnv* env, jclass clazz) {
    try {
        init_apartment();
        return OcrEngine::AvailableRecognizerLanguages().Size() > 0 ? JNI_TRUE : JNI_FALSE;
    } catch (...) {
        return JNI_FALSE;
    }
}

JNIEXPORT jobjectArray JNICALL Java_fastocr_FastOCR_getAvailableLanguages(JNIEnv* env, jclass clazz) {
    try {
        init_apartment();
        
        auto languages = OcrEngine::AvailableRecognizerLanguages();
        size_t count = languages.Size();
        
        jobjectArray result = env->NewObjectArray(count, env->FindClass("java/lang/String"), nullptr);
        
        for (size_t i = 0; i < count; i++) {
            auto lang = languages.GetAt(i);
            std::wstring langCode = lang.LanguageTag().c_str();
            
            // Convert wchar_t to UTF-8
            int size_needed = WideCharToMultiByte(CP_UTF8, 0, langCode.c_str(), -1, nullptr, 0, nullptr, nullptr);
            std::string utf8Lang(size_needed, 0);
            WideCharToMultiByte(CP_UTF8, 0, langCode.c_str(), -1, &utf8Lang[0], size_needed, nullptr, nullptr);
            
            jstring jLang = env->NewStringUTF(utf8Lang.c_str());
            env->SetObjectArrayElement(result, i, jLang);
            env->DeleteLocalRef(jLang);
        }
        
        return result;
    } catch (...) {
        return env->NewObjectArray(0, env->FindClass("java/lang/String"), nullptr);
    }
}

} // extern "C"
