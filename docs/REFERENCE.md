# FastOCR API Reference Manual

`FastOCR` provides high-performance native Windows Media OCR bindings and SIMD image preprocessing.

---

## 1. Engine Initialization

### `FastOCR` Constructor
```java
public FastOCR(String language) throws Exception
```
Initializes the native Windows Media OCR engine for the target language (e.g. `"en"` or `"de"`).

---

## 2. Text Recognition API

### `read`
```java
public OcrResult read(BufferedImage image) throws Exception
public OcrResult read(File file) throws Exception
public OcrResult read(String path) throws Exception
```
Recognizes text from the specified image input source with sub-50ms latency.
