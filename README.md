# FastOCR v0.1.0 [ALPHA] — Ultra-Fast Native OCR for Java

[![Status](https://img.shields.io/badge/status-v0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastOCR/releases/tag/v0.1.0)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe)

**⚡ A high-performance native OCR module for the FastJava ecosystem. Accelerated text recognition via Tesseract and
Windows.Media.Ocr.**

**FastOCR** delivers elite text recognition performance by leveraging hardware-accelerated native pipelines. Built for
automation bots and vision tools that need to "read" the screen in milliseconds.

[![FastKeyboard Showcase](docs/screenshot.png)](https://www.youtube.com/watch?v=BZsqQl7WqWk)

---

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [License](#license)

---

## Features

- **🔎 Native Powered**: High-speed OCR via Tesseract and Windows Media APIs.
- **⚡ SIMD Accelerated**: Optimized image pre-processing for higher accuracy.
- **📦 Zero GC Stalls**: Direct native memory access for image buffers.
- **🚀 Raw Speed**: Optimized for real-time text discovery in FastRobot.

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
    <version>v0.1.0</version>
</dependency>

<!-- FastCore (Required Native Loader) -->
<dependency>
    <groupId>com.github.andrestubbe</groupId>
    <artifactId>fastcore</artifactId>
    <version>v0.1.0</version>
</dependency>
</dependencies>
```

### Option 2: Gradle (via JitPack)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:fastocr:v0.1.0'
    implementation 'com.github.andrestubbe:fastcore:v0.1.0'
}
```

### Option 3: Direct Download (No Build Tool)

Download the latest JARs directly to add them to your classpath:

1. 📦 **[fastocr-v0.1.0.jar](https://github.com/andrestubbe/FastOCR/releases/download/v0.1.0/fastocr-v0.1.0.jar)** (The
   Core Library)
2. ⚙️ **[fastcore-v0.1.0.jar](https://github.com/andrestubbe/FastCore/releases/download/v0.1.0/fastcore-v0.1.0.jar)** (
   The Mandatory Native Loader)

> [!IMPORTANT]
> All JARs must be in your classpath for the native JNI calls to function correctly.

---

## License

MIT License — See [LICENSE](LICENSE) for details.

---
**Part of the FastJava Ecosystem** — *Making the JVM faster.*


