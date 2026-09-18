@echo off
setlocal
chcp 65001 > nul
cd /d "%~dp0"

echo ===================================================
echo  Building FastOCR Native & Main Project
echo ===================================================
call compile.bat
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Native build failed!
    pause
    exit /b %ERRORLEVEL%
)

call mvn clean install -DskipTests -q
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] FastOCR install failed!
    pause
    exit /b %ERRORLEVEL%
)

echo ===================================================
echo  Building JMH Benchmark Uber-JAR
echo ===================================================
cd examples\Benchmark
call mvn clean package -DskipTests -q
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Benchmark packaging failed!
    pause
    exit /b %ERRORLEVEL%
)

echo ===================================================
echo  Running JMH Benchmarks (Throughput: ops/s)
echo ===================================================
java "-Djava.library.path=..\..\release;..\..\src\main\resources\native" -cp "target\benchmarks.jar;..\..\target\FastOCR-0.1.1.jar" org.openjdk.jmh.Main -f 1 -wi 1 -i 2 -tu s -bm thrpt
cd ..\..
pause
