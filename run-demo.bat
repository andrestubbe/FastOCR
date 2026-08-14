@echo off
echo [FastOCR] Building Native Library...
call compile.bat
if errorlevel 1 exit /b 1

echo [FastOCR] Building Core Project...
call mvn clean package -DskipTests -q
if errorlevel 1 exit /b 1

echo [FastOCR] Running Demo...
cd examples\Demo
call mvn package -DskipTests -q
java -cp "target\demo-0.1.1.jar;..\..\target\FastOCR-0.1.1.jar" fastocr.demo.Demo
cd ..\..
pause
