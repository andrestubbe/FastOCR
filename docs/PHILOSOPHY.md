# FastOCR Design Philosophy

`FastOCR` is built around three fundamental architectural principles:

1. **Zero-Copy Memory Transfers**: Directly maps Java pixel byte buffers into Windows native COM OCR memory frames to eliminate JVM heap garbage collection pauses.
2. **Hardware Acceleration First**: Prioritizes built-in GPU/NPU Windows Media OCR pipelines and AVX2 SIMD instructions over slow CPU software OCR loops.
3. **Ergonomic Java Interface**: Provides clean AutoCloseable Java APIs without exposing C++ pointers or COM reference management complexity.
