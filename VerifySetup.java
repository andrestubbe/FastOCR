import fastocr.FastOCR;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * FastOCR Setup Verification
 * 
 * Run this to verify your FastOCR installation works correctly:
 * 1. Native library loads (fastocr.dll)
 * 2. Windows.Media.Ocr is available
 * 3. OCR engine can be created
 * 4. OCR produces results
 * 
 * Usage: java -cp "target/fastocr-1.0.0.jar;target/dependency/*" VerifySetup
 */
public class VerifySetup {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   FastOCR Setup Verification");
        System.out.println("========================================\n");
        
        boolean allPassed = true;
        
        // Test 1: Library Load
        System.out.println("[1/5] Loading native library...");
        try {
            // FastOCR static block loads the library
            Class.forName("fastocr.FastOCR");
            System.out.println("      ✓ Native library loaded\n");
        } catch (Exception e) {
            System.out.println("      ✗ Failed to load library:");
            System.out.println("        " + e.getMessage());
            System.out.println("        Make sure fastocr.dll is in the same directory as the JAR\n");
            allPassed = false;
        }
        
        // Test 2: OCR Availability
        System.out.println("[2/5] Checking OCR availability...");
        boolean available = FastOCR.isOcrAvailable();
        if (available) {
            System.out.println("      ✓ Windows.Media.Ocr is available\n");
        } else {
            System.out.println("      ✗ OCR not available");
            System.out.println("        Install Windows OCR language pack:");
            System.out.println("        Settings → Time & Language → Language → Add OCR language\n");
            allPassed = false;
        }
        
        if (!available) {
            System.out.println("========================================");
            System.out.println("   VERIFICATION FAILED");
            System.out.println("========================================");
            System.exit(1);
        }
        
        // Test 3: List Languages
        System.out.println("[3/5] Available OCR languages:");
        String[] languages = FastOCR.getSupportedLanguages();
        for (String lang : languages) {
            System.out.println("      • " + lang);
        }
        System.out.println();
        
        // Test 4: Create Engine
        System.out.println("[4/5] Creating OCR engine...");
        FastOCR ocr = null;
        try {
            ocr = new FastOCR("en");
            System.out.println("      ✓ Engine created (language: " + ocr.getLanguage() + ")\n");
        } catch (Exception e) {
            System.out.println("      ✗ Failed to create engine:");
            System.out.println("        " + e.getMessage() + "\n");
            allPassed = false;
        }
        
        if (ocr == null) {
            System.out.println("========================================");
            System.out.println("   VERIFICATION FAILED");
            System.out.println("========================================");
            System.exit(1);
        }
        
        // Test 5: OCR Performance
        System.out.println("[5/5] Running OCR performance test...");
        try {
            // Create test image
            BufferedImage testImage = createTestImage();
            
            // Warmup
            ocr.read(testImage);
            
            // Timed run
            long start = System.nanoTime();
            String result = ocr.read(testImage);
            long ms = (System.nanoTime() - start) / 1_000_000;
            
            System.out.println("      OCR Result: \"" + result + "\"");
            System.out.println("      Time: " + ms + " ms");
            
            if (ms < 100) {
                System.out.println("      ✓ Excellent performance (< 100ms)\n");
            } else if (ms < 500) {
                System.out.println("      ⚠ Acceptable performance (< 500ms)\n");
            } else {
                System.out.println("      ✗ Slow performance (> 500ms)\n");
            }
            
            ocr.close();
            
        } catch (Exception e) {
            System.out.println("      ✗ OCR test failed:");
            System.out.println("        " + e.getMessage() + "\n");
            allPassed = false;
        }
        
        // Summary
        System.out.println("========================================");
        if (allPassed) {
            System.out.println("   ✓ ALL TESTS PASSED");
            System.out.println("========================================");
            System.out.println("FastOCR is ready to use!");
            System.exit(0);
        } else {
            System.out.println("   ✗ VERIFICATION FAILED");
            System.out.println("========================================");
            System.exit(1);
        }
    }
    
    private static BufferedImage createTestImage() {
        BufferedImage img = new BufferedImage(300, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        
        // White background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 300, 80);
        
        // Black text
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("TEST", 100, 50);
        
        g.dispose();
        return img;
    }
}
