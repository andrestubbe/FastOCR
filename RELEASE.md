# FastOCR v1.0.0 Release

## 🚀 First Release - Native OCR for Java

**Release Date:** April 19, 2026

**Tag:** v1.0.0

---

## 📦 Assets

| File | Description | Size |
|------|-------------|------|
| `fastocr-1.0.0.jar` | Standard JAR (requires external DLL) | ~15 KB |
| `fastocr-1.0.0-fat.jar` | Fat JAR with embedded DLL | ~250 KB |
| `fastocr.dll` | Native library (x64) | ~200 KB |
| Source code (zip) | Full source | ~100 KB |
| Source code (tar.gz) | Full source | ~80 KB |

---

## ✨ Features

- **10-50ms OCR** — Windows.Media.Ocr GPU acceleration
- **Zero dependencies** — Uses built-in Windows 10/11 OCR
- **30+ languages** — Automatic language detection
- **Simple API** — `new FastOCR().read("image.png")`
- **Multiple input formats** — BufferedImage, File, file path
- **Cross-platform ready** — Architecture for Linux/Mac (Tesseract fallback planned)

---

## 🛠️ Installation

### Maven (JitPack)
```xml
<dependency>
    <groupId>com.github.andrestubbe</groupId>
    <artifactId>fastocr</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Fat JAR (Standalone)
```bash
# Download fastocr-1.0.0-fat.jar
java -jar fastocr-1.0.0-fat.jar
```

---

## 📝 Quick Start

```java
import fastocr.FastOCR;

// Create OCR engine
FastOCR ocr = new FastOCR("en");

// Read text from image
String text = ocr.read("screenshot.png");
System.out.println(text);

// Close when done
ocr.close();
```

---

## 🔧 Requirements

- Windows 10 version 19041 (20H1) or later
- Windows 11
- Java 17+
- OCR language pack installed (Settings → Time & Language → Language)

---

## 📊 Performance

| Library | Speed | Memory | Setup |
|---------|-------|--------|-------|
| **FastOCR** | **10-50ms** | **Zero-copy** | **None** |
| Tesseract4J | 200-500ms | 50-100MB | tessdata + install |
| JavaOCR | 500ms-2s | 100-200MB | Multiple deps |

---

## 🔗 Links

- [GitHub Repository](https://github.com/andrestubbe/FastOCR)
- [FastJava Ecosystem](https://github.com/andrestubbe/FastJava)
- [Full Documentation](https://github.com/andrestubbe/FastOCR/blob/main/README.md)
- [Issues & Bugs](https://github.com/andrestubbe/FastOCR/issues)

---

## 📜 License

MIT License — See [LICENSE](LICENSE)

---

## 🙏 Credits

- Windows.Media.Ocr team at Microsoft
- FastJava community
- JNI expertise from FastCore

**Release Manager:** Andre Stubbe  
**Build Date:** 2026-04-19
