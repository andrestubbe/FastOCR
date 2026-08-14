package fastocr.benchmark;

import fastocr.FastOCR;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class JMH_OCR {

    @Benchmark
    public void benchmarkFastOCRInit() {
        try {
            FastOCR ocr = new FastOCR("en");
            ocr.close();
        } catch (Exception ignored) {}
    }
}
