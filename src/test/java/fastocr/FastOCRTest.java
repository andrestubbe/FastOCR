package fastocr;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * JUnit 5 tests for FastOCR
 * 
 * Tests cover:
 * - OCR availability check
 * - Language enumeration
 * - Engine creation
 * - Text recognition from BufferedImage
 * - Text recognition from file
 * - Performance benchmarking
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FastOCRTest {
    
    private static final String TEST_TEXT = "HELLO OCR";
    
    /**
     * Test 1: Check OCR availability
     * This test verifies Windows.Media.Ocr is available on this system.
     */
    @Test
    @Order(1)
    @DisplayName("Check OCR availability")
    void testAvailability() {
        boolean available = FastOCR.isOcrAvailable();
        System.out.println("OCR Available: " + available);
        
        // On Windows 10/11 with OCR language pack, this should be true
        // On other systems, this will be false (expected)
        if (!available) {
            System.out.println("WARNING: OCR not available - skipping remaining tests");
        }
        
        // We don't assert true here because it depends on system configuration
        // Just verify the method doesn't crash
        assertDoesNotThrow(() -> FastOCR.isOcrAvailable());
    }
    
    /**
     * Test 2: List available languages
     */
    @Test
    @Order(2)
    @DisplayName("List supported languages")
    void testLanguages() {
        if (!FastOCR.isOcrAvailable()) {
            System.out.println("Skipping - OCR not available");
            return;
        }
        
        String[] languages = FastOCR.getSupportedLanguages();
        assertNotNull(languages);
        System.out.println("Available languages: " + languages.length);
        for (String lang : languages) {
            System.out.println("  - " + lang);
        }
    }
    
    /**
     * Test 3: Create OCR engine with default language
     */
    @Test
    @Order(3)
    @DisplayName("Create OCR engine (default language)")
    void testCreateEngineDefault() {
        if (!FastOCR.isOcrAvailable()) {
            System.out.println("Skipping - OCR not available");
            return;
        }
        
        assertDoesNotThrow(() -> {
            try (FastOCR ocr = new FastOCR()) {
                assertNotNull(ocr);
                System.out.println("Engine created with default language");
            }
        });
    }
    
    /**
     * Test 4: Create OCR engine with specific language
     */
    @Test
    @Order(4)
    @DisplayName("Create OCR engine (English)")
    void testCreateEngineEnglish() {
        if (!FastOCR.isOcrAvailable()) {
            System.out.println("Skipping - OCR not available");
            return;
        }
        
        assertDoesNotThrow(() -> {
            try (FastOCR ocr = new FastOCR("en")) {
                assertNotNull(ocr);
                assertEquals("en", ocr.getLanguage());
                System.out.println("Engine created with language: " + ocr.getLanguage());
            }
        });
    }
    
    /**
     * Test 5: OCR from BufferedImage
     * Creates a test image with known text and verifies OCR can read it.
     */
    @Test
    @Order(5)
    @DisplayName("OCR from BufferedImage")
    void testOcrFromImage() {
        if (!FastOCR.isOcrAvailable()) {
            System.out.println("Skipping - OCR not available");
            return;
        }
        
        // Create test image with text
        BufferedImage image = createTestImage(TEST_TEXT, 400, 100);
        
        try (FastOCR ocr = new FastOCR("en")) {
            String result = ocr.read(image);
            
            System.out.println("Input text:  " + TEST_TEXT);
            System.out.println("OCR result:  " + result);
            
            // Note: OCR might not be perfect, so we check for containment
            // rather than exact match
            assertNotNull(result);
            assertFalse(result.isEmpty(), "OCR returned empty text");
            
            // Check if the result contains parts of our test text (case insensitive)
            String resultUpper = result.toUpperCase();
            boolean containsHello = resultUpper.contains("HELLO");
            boolean containsOcr = resultUpper.contains("OCR");
            
            System.out.println("Contains 'HELLO': " + containsHello);
            System.out.println("Contains 'OCR': " + containsOcr);
            
            // At least one word should be recognized
            assertTrue(containsHello || containsOcr, 
                "OCR should recognize at least part of the text");
        }
    }
    
    /**
     * Test 6: OCR from file
     * Creates a temporary test file and reads it.
     */
    @Test
    @Order(6)
    @DisplayName("OCR from file")
    void testOcrFromFile() throws IOException {
        if (!FastOCR.isOcrAvailable()) {
            System.out.println("Skipping - OCR not available");
            return;
        }
        
        // Create temp file
        File tempFile = File.createTempFile("fastocr_test_", ".png");
        tempFile.deleteOnExit();
        
        // Create and save test image
        BufferedImage image = createTestImage("FILE TEST", 300, 80);
        ImageIO.write(image, "PNG", tempFile);
        
        System.out.println("Test file: " + tempFile.getAbsolutePath());
        
        try (FastOCR ocr = new FastOCR("en")) {
            String result = ocr.read(tempFile);
            
            System.out.println("File OCR result: " + result);
            
            assertNotNull(result);
            assertFalse(result.isEmpty(), "File OCR returned empty text");
        }
        
        // Cleanup
        tempFile.delete();
    }
    
    /**
     * Test 7: Performance benchmark
     * Measures average OCR time over multiple runs.
     */
    @Test
    @Order(7)
    @DisplayName("Performance benchmark")
    void testPerformance() {
        if (!FastOCR.isOcrAvailable()) {
            System.out.println("Skipping - OCR not available");
            return;
        }
        
        BufferedImage image = createTestImage("SPEED", 300, 80);
        
        try (FastOCR ocr = new FastOCR("en")) {
            // Warmup
            ocr.read(image);
            
            // Benchmark
            int iterations = 10;
            long startTime = System.nanoTime();
            
            for (int i = 0; i < iterations; i++) {
                ocr.read(image);
            }
            
            long endTime = System.nanoTime();
            long totalMs = (endTime - startTime) / 1_000_000;
            double avgMs = (double) totalMs / iterations;
            
            System.out.println("=== Performance Results ===");
            System.out.println("Iterations: " + iterations);
            System.out.println("Total time: " + totalMs + " ms");
            System.out.println("Average: " + String.format("%.2f", avgMs) + " ms");
            System.out.println("===========================");
            
            // Performance assertion: should be reasonably fast (< 500ms per image)
            assertTrue(avgMs < 500, "OCR should complete in less than 500ms on average");
        }
    }
    
    /**
     * Helper: Create test image with text
     */
    private BufferedImage createTestImage(String text, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // White background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        
        // Black text, large font for better OCR
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        
        // Center text
        int stringWidth = g2d.getFontMetrics().stringWidth(text);
        int x = (width - stringWidth) / 2;
        int y = height / 2 + 10;
        g2d.drawString(text, x, y);
        
        g2d.dispose();
        return image;
    }
}
