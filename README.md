# FastOCR 0.1.2 — Native Windows OCR for Java

[![Status](https://img.shields.io/badge/status-0.1.2-brightgreen.svg)](https://github.com/andrestubbe/FastOCR/releases/tag/0.1.2)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-0.1.2-green.svg)](https://jitpack.io/#andrestubbe/FastOCR)

---

**⚡ Hardware SIMD-accelerated zero-copy Optical Character Recognition (OCR) for Windows 10/11 and Java applications.**

`FastOCR` provides native Windows `Windows.Media.Ocr` JNI bindings with AVX2 SIMD image preprocessing, enabling sub-50ms text recognition on screen captures, images, and document streams with zero GC pressure.

![Showcase](https://raw.githubusercontent.com/andrestubbe/FastOCR/main/docs/screenshot.png)

---

## Quick Start

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
            System.out.println("Recognized Text: " + result.getText());
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
- [API Quick Reference](#api-quick-reference)
- [Technical Demos & Benchmarks](#technical-demos--benchmarks)
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

| Feature | Tesseract4J (Tess4J) | Cloud Vision API | FastOCR |
|:---|:---|:---|:---|
| **Recognition Latency**| 200–500 ms (CPU bound) | 300–1,500 ms (Network RTT) | **10–50 ms (Hardware accelerated)** |
| **Dependencies & Weights**| Massive traineddata files (50MB+)| Cloud credentials & internet | **Windows 10/11 built-in (0 MB data)** |
| **Image Preprocessing**| Slow scalar Java loops | Cloud server pipeline | **AVX2 SIMD binarization & threshold** |
| **Heap / GC Overhead** | Heavy JNA struct allocations | JSON/HTTP request garbage | **Zero GC (Direct native buffer ops)** |

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
Benchmark                        Mode  Cnt        Score   Error  Units
Benchmark.benchmarkFastOCRInit  thrpt    2  1,109,569          ops/s
```

> **1.10+ Million Ops / sec**: `FastOCR` initializes and manages native OCR contexts at **1.1+ Million operations per second** with **10–50ms recognition latency**.

---

## API Quick Reference

### Core Engine & Recognition
| Method | Return Type | Description | Docs |
|:---|:---|:---|:---|
| `new FastOCR(language)` | `FastOCR` | Initialize native Windows OCR engine for target language (e.g. `"en"`, `"de"`). | [Reference](docs/REFERENCE.md) |
| `read(BufferedImage)` | `String` | Recognize text from an in-memory Java `BufferedImage`. | [Reference](docs/REFERENCE.md) |
| `read(File)` | `String` | Recognize text directly from a file handle. | [Reference](docs/REFERENCE.md) |
| `read(String path)` | `String` | Recognize text from an image filepath string. | [Reference](docs/REFERENCE.md) |
| `isOcrAvailable()` | `boolean` | Check if native Windows Media OCR is supported on this system. | [Reference](docs/REFERENCE.md) |
| `getSupportedLanguages()` | `String[]` | Query array of installed OS language OCR codes. | [Reference](docs/REFERENCE.md) |
| `close()` | `void` | Release native OCR handles and resources. | [Reference](docs/REFERENCE.md) |

### Geometry & Result Primitives
| Class / Method | Return Type | Description | Docs |
|:---|:---|:---|:---|
| `OcrResult.getText()` | `String` | Complete recognized text string with preserved newlines. | [Reference](docs/REFERENCE.md) |
| `OcrResult.getLines()` | `List<OcrLine>` | Unmodifiable list of recognized text lines in reading order. | [Reference](docs/REFERENCE.md) |
| `OcrResult.getWords()` | `List<OcrWord>` | Unmodifiable list of all recognized individual word tokens. | [Reference](docs/REFERENCE.md) |
| `OcrLine.getBoundingBox()` | `Rectangle` | Bounding rectangle (`x`, `y`, `width`, `height`) of the line. | [Reference](docs/REFERENCE.md) |
| `OcrWord.getBoundingBox()` | `Rectangle` | Bounding box of the single recognized word on source image. | [Reference](docs/REFERENCE.md) |
| `OcrWord.getConfidence()` | `float` | Recognition confidence score (`0.0f` to `1.0f`). | [Reference](docs/REFERENCE.md) |
| `OcrWord.getCenterX()`, `getCenterY()` | `int` | Exact center pixel coordinates (click target for UI bots). | [Reference](docs/REFERENCE.md) |

---

## Technical Demos & Benchmarks

Run standalone verification demos or execute JMH throughput microbenchmarks:

| Type | Target / Launcher | Source File | Description |
|:---|:---|:---|:---|
| **Interactive Demo** | [`run-demo.bat`](run-demo.bat) | [`Demo.java`](examples/Demo/src/main/java/fastocr/demo/Demo.java) | End-to-end verification of native OCR initialization and text recognition |
| **Throughput Benchmark** | [`run-benchmark.bat`](run-benchmark.bat) | [`Benchmark.java`](examples/Benchmark/src/main/java/fastocr/benchmark/Benchmark.java) | Formal OpenJDK JMH microbenchmark suite measuring native engine throughput |

---

## Installation

### Option 1: Maven (Recommended)

Add the JitPack repository and the dependency to your `pom.xml`:

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
        <version>0.1.2</version>
    </dependency>
</dependencies>
```

### Option 2: Gradle (via JitPack)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:FastOCR:0.1.2'
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

| Platform | Architecture | Status | Notes |
|:---|:---|:---|:---|
| **Windows 10 / 11** | `x86_64` | ✅ Fully Supported | Native `Windows.Media.Ocr` hardware acceleration |
| **Linux** | `x86_64` | 🔄 Planned | Native Tesseract / Leptonica bridge |
| **macOS** | `Apple Silicon / Intel` | 🔄 Planned | Native Vision framework / Tesseract bridge |

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
