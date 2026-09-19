# FastOCR Compilation Guide

## Native C++ MSVC AVX2 Build Chain

1. Requirements: Visual Studio 2022 / 2026 with "Desktop development with C++" and JDK 17+.
2. Open Developer Command Prompt or PowerShell in the repository root.
3. Run the automated native compilation script:

```cmd
compile.bat
```

This compiles the real `src/main/c++/fastocr.cpp` Windows.Media.Ocr JNI implementation with MSVC C++17/exception handling and AVX2 flags (`/std:c++17 /EHsc /arch:AVX2 /O2 /D_CRT_SECURE_NO_WARNINGS`), then copies the output DLL to the root and architecture-specific resource locations used by the Java loader.

The former `fastocr_stub.cpp` placeholder is no longer part of the build.
