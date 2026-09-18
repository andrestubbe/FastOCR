@echo off
setlocal
chcp 65001 > nul
cd /d "%~dp0"

echo ===================================================
echo  FastOCR Demo
echo ===================================================
echo [1/3] Building Native Library...
call compile.bat
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Native build failed!
    pause
    exit /b %ERRORLEVEL%
)

echo [2/3] Building Core Project...
call mvn clean install -DskipTests -q
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Core build failed!
    pause
    exit /b %ERRORLEVEL%
)

echo [3/3] Running Demo...
cd examples\Demo
call mvn clean package -DskipTests -q
java "-Djava.library.path=..\..\release;..\..\src\main\resources\native" -cp "target\demo-0.1.1.jar;..\..\target\FastOCR-0.1.1.jar" fastocr.demo.Demo
cd ..\..
pause
