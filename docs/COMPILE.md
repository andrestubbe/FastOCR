# FastOCR Compilation Guide

## Native C++ MSVC AVX2 Build Chain

1. Requirements: Visual Studio 2022 / 2026 with "Desktop development with C++" and JDK 17+.
2. Open Developer Command Prompt or PowerShell in the repository root.
3. Run the automated native compilation script:

```cmd
compile.bat
```

This compiles `fastocr.dll` with MSVC AVX2 flags (`/arch:AVX2 /O2 /D_CRT_SECURE_NO_WARNINGS`) and copies the output DLL to `src/main/resources/native/` and `src/main/resources/win32-x86-64/`.
