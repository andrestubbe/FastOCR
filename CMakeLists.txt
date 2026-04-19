cmake_minimum_required(VERSION 3.16)
project(FastOCR VERSION 1.0.0 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Windows-specific settings
if(WIN32)
    # Enable WinRT support
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /ZW /EHsc /await")
    
    # Find Windows SDK
    find_package(WindowsSDK REQUIRED)
    
    # JNI headers
    find_package(JNI REQUIRED)
    include_directories(${JNI_INCLUDE_DIRS})
    
    # Source files
    set(SOURCES
        src/main/c++/fastocr.cpp
    )
    
    # Create DLL
    add_library(fastocr SHARED ${SOURCES})
    
    # Link libraries
    target_link_libraries(fastocr
        ${JNI_LIBRARIES}
        WindowsApp.lib
        Windowscodecs.lib
    )
    
    # Set output name
    set_target_properties(fastocr PROPERTIES
        PREFIX ""
        OUTPUT_NAME "fastocr"
        RUNTIME_OUTPUT_DIRECTORY ${CMAKE_SOURCE_DIR}/build
    )
    
    # Windows 10 19041+ (20H1) required for WinRT OCR
    target_compile_definitions(fastocr PRIVATE
        _WIN32_WINNT=0x0A00
        WINVER=0x0A00
        NTDDI_VERSION=0x0A000007
    )
endif()

# Install
install(TARGETS fastocr
    RUNTIME DESTINATION build
)
