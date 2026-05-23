@echo off
echo ⚡ Building Main Project...
call mvn clean package -DskipTests
if %ERRORLEVEL% NEQ 0 ( pause & exit /b )
echo 🚀 Running Hero Demo...
cd examples\00-basic-usage
call mvn compile exec:java -Dexec.mainClass=BasicOcrExample
cd ..\..
pause
