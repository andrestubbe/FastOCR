@echo off
setlocal enabledelayedexpansion
set PROJECT_NAME=fastocr

echo ========================================
echo FastOCR Native Library Builder (FastJava Standard)
echo ========================================

set "VSWHERE=%ProgramFiles(x86)%\Microsoft Visual Studio\Installer\vswhere.exe"
if not exist "!VSWHERE!" set "VSWHERE=%ProgramFiles%\Microsoft Visual Studio\Installer\vswhere.exe"

if exist "!VSWHERE!" (
    for /f "usebackq tokens=*" %%i in (`"!VSWHERE!" -latest -products * -requires Microsoft.VisualStudio.Component.VC.Tools.x86.x64 -property installationPath`) do (
        set "VS_PATH=%%i"
    )
)

if not defined VS_PATH (
    if exist "C:\Program Files\Microsoft Visual Studio\18\Community" set "VS_PATH=C:\Program Files\Microsoft Visual Studio\18\Community"
)

if not defined VS_PATH (
    echo [ERROR] Visual Studio with C++ tools not found!
    exit /b 1
)

echo Found Visual Studio at: !VS_PATH!

if not defined JAVA_HOME (
    if exist "C:\Program Files\Java\jdk-21.0.12.1" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.12.1"
    ) else if exist "C:\Program Files\Java\jdk-25" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-25"
    ) else if exist "C:\Program Files\Java\jdk-17" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-17"
    )
)

if not defined JAVA_HOME (
    echo [ERROR] JAVA_HOME not found!
    exit /b 1
)

echo Using JAVA_HOME: !JAVA_HOME!

call "!VS_PATH!\VC\Auxiliary\Build\vcvars64.bat"

if not exist "build" mkdir build
if not exist "release" mkdir release
if not exist "src\main\resources\native" mkdir "src\main\resources\native"
if not exist "src\main\resources\win32-x86-64" mkdir "src\main\resources\win32-x86-64"
if not exist "target\classes\native" mkdir "target\classes\native"
set "FASTCORE_DIR=%USERPROFILE%\.fastcore\native\%PROJECT_NAME%"
if not exist "!FASTCORE_DIR!" mkdir "!FASTCORE_DIR!"

cl.exe /nologo /O2 /arch:AVX2 /std:c++17 /MD /LD /D_CRT_SECURE_NO_WARNINGS ^
    /I"!JAVA_HOME!\include" ^
    /I"!JAVA_HOME!\include\win32" ^
    /I"..\FastSIMD\src\main\native" ^
    src\main\c++\fastocr_stub.cpp ^
    /Fo:build\fastocr.obj ^
    /link /DLL /OUT:release\fastocr.dll user32.lib gdi32.lib shcore.lib advapi32.lib dwmapi.lib

if errorlevel 1 (
    echo [ERROR] Compilation failed!
    exit /b 1
)

copy /Y release\fastocr.dll build\fastocr.dll >nul
copy /Y release\fastocr.dll src\main\resources\native\fastocr.dll >nul
copy /Y release\fastocr.dll src\main\resources\win32-x86-64\fastocr.dll >nul
copy /Y release\fastocr.dll target\classes\native\fastocr.dll >nul 2>&1
copy /Y release\fastocr.dll "!FASTCORE_DIR!\fastocr.dll" >nul
powershell -NoProfile -Command "Unblock-File -Path '!FASTCORE_DIR!\fastocr.dll', 'release\fastocr.dll', 'src\main\resources\native\fastocr.dll' -ErrorAction SilentlyContinue" >nul 2>&1

echo.
echo ===========================================
echo [SUCCESS] FastOCR native DLL built!
echo Copied to release\, resources\, and .fastcore
echo ===========================================
