# FastOCR 0.1.0 [ALPHA]  Ultra-Fast Native OCR for Java

[![Status](https://img.shields.io/badge/status-0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastOCR/releases/tag/0.1.0)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe)

---

**âš¡ A high-performance native OCR module for the FastJava ecosystem. Accelerated text recognition via Tesseract and
Windows.Media.Ocr.**

**FastOCR** delivers elite text recognition performance by leveraging hardware-accelerated native pipelines. Built for
automation bots and vision tools that need to "read" the screen in milliseconds.

---

[![FastKeyboard Showcase](docs/screenshot.png)](https://www.youtube.com/watch?v=BZsqQl7WqWk)

---

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [License](#license)

---

## Features

- **ðŸš€ Native Powered**: High-speed OCR via Tesseract and Windows Media APIs.
- **? SIMD Accelerated**: Optimized image pre-processing for higher accuracy.
- **ðŸš€ Zero GC Stalls**: Direct native memory access for image buffers.
- **ðŸš€ Raw Speed**: Optimized for real-time text discovery in FastRobot.

---

## Installation

### Option 1: Maven (Recommended)

Add the JitPack repository and the dependencies to your `pom.xml`:

```xml

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
<!-- FastOCR Library -->
<dependency>
    <groupId>com.github.andrestubbe</groupId>
    <artifactId>fastocr</artifactId>
    <version>0.1.0</version>
</dependency>

<!-- FastCore (Required Native Loader) -->
<dependency>
    <groupId>com.github.andrestubbe</groupId>
    <artifactId>fastcore</artifactId>
    <version>0.1.0</version>
</dependency>
</dependencies>
```

### Option 2: Gradle (via JitPack)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:fastocr:0.1.0'
    implementation 'com.github.andrestubbe:fastcore:0.1.0'
}
```

### Option 3: Direct Download (No Build Tool)

Download the latest JARs directly to add them to your classpath:

1. ðŸš€ **[fastocr-0.1.0.jar](https://github.com/andrestubbe/FastOCR/releases/download/0.1.0/fastocr-0.1.0.jar)** (The
   Core Library)
2. ðŸš€ **[fastcore-0.1.0.jar](https://github.com/andrestubbe/FastCore/releases/download/0.1.0/fastcore-0.1.0.jar)** (
   The Mandatory Native Loader)

> [!IMPORTANT]
> All JARs must be in your classpath for the native JNI calls to function correctly.

---

## Documentation

* **[COMPILE.md](COMPILE.md)**: Full compilation guide (MSVC C++17 build chain + JNI Setup).
* **[REFERENCE.md](docs/REFERENCE.md)**: Full API descriptions, border configurations, and codepoint index.
* **[PHILOSOPHY.md](docs/PHILOSOPHY.md)**: The engineering rationale for zero-allocation performance.
* **[ROADMAP.md](docs/ROADMAP.md)**: Future milestones and planned features.

---

## Platform Support

| Platform      | Status            |
|---------------|-------------------|
| Windows 10/11 | ? Fully Supported |
| Linux         | ðŸš€ Planned        |
| macOS         | ðŸš€ Planned        |

---

## License

MIT License  See [LICENSE](LICENSE) for details.

---


## Related Projects

- [FastCore](https://github.com/andrestubbe/FastCore)  Native Library Loader for Java
- [FastAudioPlayer](https://github.com/andrestubbe/FastAudioPlayer)  Native Windows WASAPI Audio Playback for Java
- [FastTTS](https://github.com/andrestubbe/FastTTS)  High-Performance Native Windows TTS API for Java
- [FastSTT](https://github.com/andrestubbe/FastSTT)  Ultra-Fast Native Speech-to-Text for Java
- [FastWakeWord](https://github.com/andrestubbe/FastWakeWord)

---

**Part of the FastJava Ecosystem**  *Making the JVM faster. Small package. Maximum speed. Zero bloat. ðŸš€ðŸš€*




