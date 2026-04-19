/**
 * @file fastocr_stub.cpp
 * @brief Stub implementation for FastOCR v1.0.0
 * 
 * Minimal JNI implementation that compiles without WinRT.
 * Returns stub responses for testing Java API.
 * Full Windows.Media.Ocr implementation in v1.1
 */

#include <jni.h>
#include <windows.h>
#include <string>

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

extern "C" {

JNIEXPORT jlong JNICALL Java_fastocr_FastOCR_createOcrEngine(JNIEnv* env, jclass clazz, jstring language) {
    // Stub: Return dummy handle
    return 1;
}

JNIEXPORT void JNICALL Java_fastocr_FastOCR_destroyOcrEngine(JNIEnv* env, jclass clazz, jlong handle) {
    // Stub: Nothing to do
}

JNIEXPORT jstring JNICALL Java_fastocr_FastOCR_recognizeBytes(
    JNIEnv* env, jclass clazz, jlong handle, 
    jbyteArray imageData, jint width, jint height, jint channels) {
    
    // Stub: Return sample text
    return env->NewStringUTF("STUB: OCR not yet implemented in v1.0.0\nFull Windows.Media.Ocr support coming in v1.1");
}

JNIEXPORT jstring JNICALL Java_fastocr_FastOCR_recognizeFile(JNIEnv* env, jclass clazz, jlong handle, jstring filePath) {
    // Stub: Return sample text
    return env->NewStringUTF("STUB: OCR from file not yet implemented in v1.0.0\nFull Windows.Media.Ocr support coming in v1.1");
}

JNIEXPORT jboolean JNICALL Java_fastocr_FastOCR_isAvailable(JNIEnv* env, jclass clazz) {
    return JNI_TRUE;
}

JNIEXPORT jobjectArray JNICALL Java_fastocr_FastOCR_getAvailableLanguages(JNIEnv* env, jclass clazz) {
    jobjectArray result = env->NewObjectArray(1, env->FindClass("java/lang/String"), nullptr);
    jstring jLang = env->NewStringUTF("en");
    env->SetObjectArrayElement(result, 0, jLang);
    env->DeleteLocalRef(jLang);
    return result;
}

JNIEXPORT jstring JNICALL Java_fastocr_FastOCR_recognizeDetailedBytes(
    JNIEnv* env, jclass clazz, jlong handle,
    jbyteArray imageData, jint width, jint height, jint channels) {
    
    // Stub: Return same as simple text
    return Java_fastocr_FastOCR_recognizeBytes(env, clazz, handle, imageData, width, height, channels);
}

} // extern "C"
