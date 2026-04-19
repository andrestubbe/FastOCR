@echo off
REM Build script for FastOCR native DLL
REM Requires: Visual Studio 2019+ with C++ workload

echo === FastOCR Native Build ===
echo.

REM Find Visual Studio installation
set "VSWHERE=%ProgramFiles(x86)%\Microsoft Visual Studio\Installer\vswhere.exe"
if not exist "%VSWHERE%" (
    echo ERROR: vswhere.exe not found. Install Visual Studio 2019+.
    exit /b 1
)

REM Get VS installation path
for /f "usebackq tokens=*" %%i in (`"%VSWHERE%" -latest -products * -requires Microsoft.VisualStudio.Component.VC.Tools.x86.x64 -property installationPath`) do (
    set "VSPATH=%%i"
)

if not defined VSPATH (
    echo ERROR: Visual Studio with C++ tools not found.
    exit /b 1
)

echo Found Visual Studio at: %VSPATH%

REM Setup environment
call "%VSPATH%\VC\Auxiliary\Build\vcvars64.bat"
if errorlevel 1 (
    echo ERROR: Failed to setup build environment
    exit /b 1
)

REM Create build directory
if not exist build mkdir build
cd build

echo.
echo Building fastocr.dll...
echo.

REM Compile with cl.exe
REM Note: This is a simplified version. Real build needs proper WinRT support
cl.exe ^
    /nologo ^
    /W3 ^
    /O2 ^
    /MD ^
    /EHsc ^
    /std:c++17 ^
    /I"%JAVA_HOME%\include" ^
    /I"%JAVA_HOME%\include\win32" ^
    /I"%WindowsSdkDir%Include\%WindowsSDKVersion%um" ^
    /I"%WindowsSdkDir%Include\%WindowsSDKVersion%shared" ^
    /I"%WindowsSdkDir%Include\%WindowsSDKVersion%ucrt" ^
    /I"%VSPATH%\VC\Tools\MSVC\%VCToolsVersion%\include" ^
    ..\src\main\c++\fastocr.cpp ^
    /link ^
    /DLL ^
    /OUT:fastocr.dll ^
    WindowsApp.lib ^
    Windowscodecs.lib ^
    kernel32.lib ^
    user32.lib

if errorlevel 1 (
    echo.
    echo ERROR: Build failed
    exit /b 1
)

echo.
echo === Build successful ===
echo Output: build\fastocr.dll
echo.
echo Now run: mvn clean package
cd ..
