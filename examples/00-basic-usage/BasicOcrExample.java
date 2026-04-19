import fastocr.FastOCR;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Basic FastOCR Example
 * 
 * Demonstrates:
 * - Checking OCR availability
 * - Reading text from image file
 * - Reading text from BufferedImage
 * - Listing available languages
 */
public class BasicOcrExample {
    
    public static void main(String[] args) {
        System.out.println("=== FastOCR Basic Example ===\n");
        
        // 1. Check if OCR is available
        if (!FastOCR.isOcrAvailable()) {
            System.err.println("OCR not available on this system!");
            System.err.println("Windows 10/11 required with OCR language pack installed.");
            return;
        }
        
        System.out.println("✓ OCR is available\n");
        
        // 2. List available languages
        System.out.println("Available languages:");
        String[] languages = FastOCR.getSupportedLanguages();
        for (String lang : languages) {
            System.out.println("  - " + lang);
        }
        System.out.println();
        
        // 3. Create OCR engine (English)
        try (FastOCR ocr = new FastOCR("en")) {
            System.out.println("✓ OCR engine created (English)\n");
            
            // 4. Read from file (if provided)
            if (args.length > 0) {
                String imagePath = args[0];
                System.out.println("Reading: " + imagePath);
                
                String text = ocr.read(imagePath);
                
                System.out.println("\n--- Recognized Text ---");
                System.out.println(text);
                System.out.println("------------------------\n");
            } else {
                System.out.println("Usage: java BasicOcrExample <image.png>");
                System.out.println("No image provided - skipping file OCR\n");
            }
            
            // 5. Read from BufferedImage (create simple test image)
            BufferedImage testImage = createTestImage();
            System.out.println("Testing with in-memory image...");
            
            String textFromImage = ocr.read(testImage);
            System.out.println("Result: '" + textFromImage + "'\n");
            
        } catch (Exception e) {
            System.err.println("OCR Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== Done ===");
    }
    
    /**
     * Creates a simple test image with text
     * (In real usage, load actual image files)
     */
    private static BufferedImage createTestImage() {
        // Create 200x50 white image
        BufferedImage image = new BufferedImage(200, 50, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g2d = image.createGraphics();
        
        // White background
        g2d.setColor(java.awt.Color.WHITE);
        g2d.fillRect(0, 0, 200, 50);
        
        // Black text
        g2d.setColor(java.awt.Color.BLACK);
        g2d.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        g2d.drawString("HELLO", 60, 35);
        
        g2d.dispose();
        return image;
    }
}
