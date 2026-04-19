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

REM Find Java if JAVA_HOME not set
if not defined JAVA_HOME (
    if exist "%ProgramFiles%\Java\jdk-25" (
        set "JAVA_HOME=%ProgramFiles%\Java\jdk-25"
    ) else (
        for /f "usebackq tokens=*" %%j in (`dir "%ProgramFiles%\Java\jdk*" /b 2^>nul`) do (
            set "JAVA_HOME=%ProgramFiles%\Java\%%j"
            goto :javaFound
        )
    )
)
:javaFound

if not defined JAVA_HOME (
    echo ERROR: JAVA_HOME not set and no JDK found.
    echo Please install Java JDK 17+ or set JAVA_HOME environment variable.
    exit /b 1
)

echo Using Java at: %JAVA_HOME%

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
    /wd4005 ^
    /I"%JAVA_HOME%\include" ^
    /I"%JAVA_HOME%\include\win32" ^
    /I"%WindowsSdkDir%Include\%WindowsSDKVersion%um" ^
    /I"%WindowsSdkDir%Include\%WindowsSDKVersion%shared" ^
    /I"%WindowsSdkDir%Include\%WindowsSDKVersion%ucrt" ^
    /I"%VSPATH%\VC\Tools\MSVC\%VCToolsVersion%\include" ^
    /I"%WindowsSdkDir%Include\%WindowsSDKVersion%cppwinrt" ^
    /I"%WindowsSdkDir%Include\%WindowsSDKVersion%winrt" ^
    ..\src\main\c++\fastocr_stub.cpp ^
    /link ^
    /DLL ^
    /OUT:fastocr.dll ^
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
