@echo off
echo Building Main Project...
call compile.bat
call mvn clean package -DskipTests -q

echo Building Benchmark Uber-JAR...
cd examples\Benchmark
call mvn clean package -DskipTests -q

echo Running JMH Benchmarks...
java -cp "target\benchmarks.jar;..\..\target\FastOCR-0.1.1.jar" org.openjdk.jmh.Main -f 1 -i 2 -wi 1 -w 1s -r 1s

cd ..\..
pause
