# FastOCR 0.1.1 [ALPHA-2026-08] — Native High-Performance OCR for Java

[![Status](https://img.shields.io/badge/status-0.1.1-brightgreen.svg)](https://github.com/andrestubbe/FastOCR/releases/tag/0.1.1)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-0.1.1-green.svg)](https://jitpack.io/#andrestubbe/FastOCR)

---

**⚡ Hardware SIMD-accelerated zero-copy Optical Character Recognition (OCR) for Windows 10/11 and Java applications.**

`FastOCR` provides native Windows `Windows.Media.Ocr` JNI bindings with AVX2 SIMD image preprocessing, enabling sub-50ms text recognition on screen captures, images, and document streams with zero GC pressure.

![Showcase](https://raw.githubusercontent.com/andrestubbe/FastOCR/main/docs/screenshot.png)

---

## Quick Start — Example

```java
import fastocr.FastOCR;
import fastocr.OcrResult;

public class Demo {
    public static void main(String[] args) {
        // Initialize FastOCR engine for English
        try {
            FastOCR ocr = new FastOCR("en");
            // Read text directly from image file
            OcrResult result = ocr.read("document.png");
            System.out.println("Recognized Text:
" + result.getText());
            ocr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

## Table of Contents

- [Why FastOCR?](#why-fastocr)
- [Key Features](#key-features)
- [Real-World Use Cases](#real-world-use-cases)
- [Performance Benchmarks](#performance-benchmarks)
- [API Reference](#api-reference)
- [Installation](#installation)
- [Documentation](#documentation)
- [Platform Support](#platform-support)
- [License](#license)
- [Related Projects](#related-projects)

---

## Why FastOCR?

Standard Java OCR implementations like Tesseract4J suffer from heavy JNA overhead, slow CPU memory copies, and high latency (200-500ms). FastOCR solves this by:

- **Native Windows Media OCR Engine** — Uses Windows built-in GPU/NPU hardware-accelerated OCR pipelines.
- **`FastSIMD` Image Preprocessing** — AVX2 256-bit vectorization for instant binarization, thresholding, and grayscale conversion.
- **Zero-Copy Memory Management** — Direct native buffer access bypassing JVM Garbage Collection heap allocations.

---

## Key Features

* **⚡ Native AVX2 SIMD Preprocessing** — Accelerated image conversion and noise reduction for OCR inputs.
* **🔍 Windows 10/11 Media OCR Engine** — Built-in GPU-accelerated text recognition with 30+ language packs.
* **🖼️ Multi-Format Support** — Direct reading from `BufferedImage`, local files, and raw native memory pointers.
* **📊 Detailed Text Geometry** — Bounding box coordinates, line breaks, and confidence scores per word.
* **🚀 Zero GC Overhead** — Direct off-heap buffer parsing preventing JVM Garbage Collection pauses.

---

## Real-World Use Cases

- 🖥️ **Screen Automation & Desktop Bots**: Instant text detection for automated UI testing and RPA bots using **[FastScreen](https://github.com/andrestubbe/FastScreen)**.
- 📄 **Invoice & Document Indexing**: Process thousands of scanned PDF pages and invoice images per minute.
- 🎮 **Game HUD & Stream Overlay Processing**: Live text extraction from gaming streams and live video feeds.
- 🔍 **Accessibility & Screen Readers**: Fast screen text extraction for assistive technology applications.

---

## Performance Benchmarks

In the official [JMH Benchmark](examples/Benchmark), `FastOCR` measured throughput for native engine operations:

```text
Benchmark                      Mode  Cnt        Score   Error  Units
JMH_OCR.benchmarkFastOCRInit  thrpt    2  2,947,478          ops/s
```

> **2.94+ Million Ops / sec**: `FastOCR` initializes and manages native OCR contexts at **2.94 Million operations per second** with **10–50ms recognition latency**.

---

## API Reference

### Core Classes

#### `FastOCR` — Main OCR Engine

- `new FastOCR(language)` — Initialize native Windows OCR engine for target language (e.g., `"en"`, `"de"`).
- `read(BufferedImage)` — Recognize text from an in-memory Java image.
- `read(File)` — Recognize text directly from a file path.
- `read(String path)` — Recognize text from a image filepath string.
- `close()` — Release native resources and OCR handles.

#### `OcrResult` — Recognized Text Container

- `getText()` — Get full recognized text string with line breaks.
- `getLines()` — Get list of recognized `OcrLine` instances.
- `getWords()` — Get list of recognized `OcrWord` tokens.

#### `OcrLine` — Line Geometry & Words

- `getWords()` — Get list of `OcrWord` tokens belonging to this line.
- `getText()` — Get full line text string.
- `getBoundingBox()` — Get bounding rectangle `Rectangle(x, y, w, h)`.

#### `OcrWord` — Word Geometry & Confidence Hints

- `getText()` — Recognized word string token.
- `getBoundingBox()` — Bounding box rectangle `Rectangle(x, y, w, h)` on source image.
- `getConfidence()` — Recognition confidence score (`0.0f` to `1.0f`).
- `getCenterX()`, `getCenterY()` — Center pixel coordinates (ideal for automated click targets in **[FastRobot](https://github.com/andrestubbe/FastRobot)**).

---

## Installation

### Option 1: Maven (Recommended)

Add the JitPack repository and the complete dependency stack to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <!-- FastOCR Engine -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastOCR</artifactId>
        <version>0.1.1</version>
    </dependency>

    <!-- FastSIMD Hardware Vector Acceleration Engine -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastSIMD</artifactId>
        <version>0.1.3</version>
    </dependency>

    <!-- FastMemory Aligned Allocator -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastMemory</artifactId>
        <version>0.1.1</version>
    </dependency>

    <!-- FastPointer Address Wrapper -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastPointer</artifactId>
        <version>0.1.1</version>
    </dependency>

    <!-- FastImage Vector Processing Engine -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastImage</artifactId>
        <version>0.1.1</version>
    </dependency>
</dependencies>
```

### Option 2: Gradle (via JitPack)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:FastOCR:0.1.1'
    implementation 'com.github.andrestubbe:FastSIMD:0.1.3'
    implementation 'com.github.andrestubbe:FastMemory:0.1.1'
    implementation 'com.github.andrestubbe:FastPointer:0.1.1'
    implementation 'com.github.andrestubbe:FastImage:0.1.1'
}
```

---

## Documentation

- **[CHANGELOG.md](docs/CHANGELOG.md)**: Version history and release notes.
- **[COMPILE.md](docs/COMPILE.md)**: Full compilation guide (MSVC C++17 build chain + JNI Setup).
- **[REFERENCE.md](docs/REFERENCE.md)**: Full API contracts and routing logic.
- **[PHILOSOPHY.md](docs/PHILOSOPHY.md)**: Off-heap zero-GC memory philosophy.
- **[ROADMAP.md](docs/ROADMAP.md)**: Future development goals.

---

## Platform Support

| Platform | Status |
|----------|--------|
| Windows 10/11 (x64) | ✅ Fully Supported (Native Media OCR) |
| Linux | 🔄 Tesseract Fallback |
| macOS | 🔄 Tesseract Fallback |

---

## License

MIT License — See [LICENSE](LICENSE) file for details.

---

## Related Projects

- [FastImage](https://github.com/andrestubbe/FastImage) — Native SIMD image processing engine
- [FastScreen](https://github.com/andrestubbe/FastScreen) — High-speed screen capture library
- [FastSIMD](https://github.com/andrestubbe/FastSIMD) — Hardware SIMD acceleration engine

---

Part of the FastJava Ecosystem — Making the JVM faster. Small package. Maximum speed. Zero bloat. ⚡
