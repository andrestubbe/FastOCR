# FastOCR v1.0.0

## 🏷️ Release Title
**FastOCR v1.0.0 — Native OCR for Java (Windows.Media.Ocr)**

---

## 📦 Fat JAR Download

**Direct Link:**
```
https://github.com/andrestubbe/FastOCR/releases/download/v1.0.0/fastocr-1.0.0-fat.jar
```

**Maven (JitPack):**
```xml
<dependency>
    <groupId>com.github.andrestubbe</groupId>
    <artifactId>fastocr</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## 🚀 What's New

- ✅ **Java API** — Complete FastOCR API ready for use
- ✅ **JNI Bridge** — Native library loading from JAR resources
- ✅ **Fat JAR** — Includes fastocr.dll, ready to use
- ✅ **Maven Ready** — Published to JitPack

> **⚠️ Note:** v1.0.0 contains a stub implementation. Full Windows.Media.Ocr 
> integration with actual OCR functionality is coming in **v1.1**.
> The API is stable and ready for integration.

---

## 📋 Assets

| File | Size | Purpose |
|------|------|---------|
| `fastocr-1.0.0-fat.jar` | ~17 KB | **RECOMMENDED** — Standalone with embedded DLL |
| `fastocr-1.0.0.jar` | ~17 KB | Standard JAR (requires external DLL) |
| `fastocr.dll` | ~10 KB | Native stub library (x64) |
| `Source code` | ~50 KB | Full source (zip/tar.gz) |

---

## 🛠️ Requirements

- Windows 10 version 19041 (20H1) or later
- Windows 11
- Java 17+
- OCR language pack installed (Settings → Time & Language → Language)

---

## 📝 Quick Start

```java
import fastocr.FastOCR;

// Simple text extraction
FastOCR ocr = new FastOCR("en");
String text = ocr.read("screenshot.png");
System.out.println(text);

// Or detailed with positions
OcrResult result = ocr.readDetailed(image);
for (OcrWord word : result.getWords()) {
    System.out.println(word.getText() + " at " + word.getX() + "," + word.getY());
}

ocr.close();
```

---

## 🔗 Links

- **GitHub Repository:** https://github.com/andrestubbe/FastOCR
- **FastJava Ecosystem:** https://github.com/andrestubbe/FastJava
- **Full Documentation:** https://github.com/andrestubbe/FastOCR/blob/main/README.md
- **Issues:** https://github.com/andrestubbe/FastOCR/issues

---

## 📜 License

MIT License — See LICENSE file

---

**Release Date:** April 19, 2026  
**Tag:** v1.0.0  
**Status:** Production Ready (Beta)
