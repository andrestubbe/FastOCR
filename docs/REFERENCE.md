# FastOCR API Reference Manual

`FastOCR` provides high-performance native Windows Media OCR JNI bindings, GPU-accelerated text recognition, and AVX2 SIMD image preprocessing.

---

## 1. FastOCR Engine API

### `FastOCR(String language)`
```java
public FastOCR(String language) throws Exception
```
Constructs and initializes the native Windows Media OCR engine for the requested language pack.

#### Parameters:
- **`language`** (`String`): Language tag identifier (e.g., `"en"`, `"de"`, `"fr"`, `"es"`).

#### Throws:
- **`Exception`**: If the native library fails to load or the specified language pack is not installed on the system.

---

### `read(BufferedImage image)`
```java
public OcrResult read(BufferedImage image) throws Exception
```
Performs text recognition on an in-memory Java `BufferedImage` using direct native pixel buffer mapping.

---

### `read(File file)`
```java
public OcrResult read(File file) throws Exception
```
Recognizes text directly from an image file on disk (`.png`, `.jpg`, `.bmp`).

---

### `read(String path)`
```java
public OcrResult read(String path) throws Exception
```
Recognizes text directly from an image filepath string.

---

### `close()`
```java
public void close()
```
Releases all underlying native C++ and Windows Media OCR COM engine handles.

---

## 2. OcrResult Container API

### `getText()`
```java
public String getText()
```
Returns the full recognized text content formatted with newline separators between lines.

---

### `getLines()`
```java
public List<OcrLine> getLines()
```
Returns an unmodifiable list of `OcrLine` instances in reading order.

---

### `getWords()`
```java
public List<OcrWord> getWords()
```
Returns a flattened list of all `OcrWord` tokens across all lines in reading order.

---

## 3. Geometry & Metadata API (`OcrLine` & `OcrWord`)

### `getBoundingBox()`
```java
public Rectangle getBoundingBox()
```
Returns the bounding rectangle (`java.awt.Rectangle`) specifying `x`, `y`, `width`, and `height` of the text element on the source image.

---

### `getConfidence()`
```java
public float getConfidence()
```
Returns the recognition confidence score ranging from `0.0f` (uncertain) to `1.0f` (certain).
