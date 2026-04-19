package fastocr;

/**
 * Represents a single recognized word with position and confidence.
 * 
 * <p>Windows.Media.Ocr provides detailed information for each word including
 * bounding box coordinates and recognition confidence. This enables
 * precise interaction with recognized text (clicking, highlighting, etc.).</p>
 * 
 * <p><b>Example:</b></p>
 * <pre>{@code
 * OcrResult result = ocr.readDetailed(image);
 * for (OcrWord word : result.getWords()) {
 *     System.out.println(word.getText() + " at (" + word.getX() + ", " + word.getY() + ")");
 *     // Click on the word:
 *     robot.mouseMove(word.getCenterX(), word.getCenterY());
 * }
 * }</pre>
 * 
 * @author FastJava Team
 * @version 1.0.0
 * @since 1.0.0
 */
public class OcrWord {
    
    private final String text;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final double confidence;
    
    /**
     * Creates an OCR word result.
     * 
     * @param text the recognized text
     * @param x left coordinate of bounding box
     * @param y top coordinate of bounding box  
     * @param width width of bounding box
     * @param height height of bounding box
     * @param confidence recognition confidence (0.0-1.0)
     */
    public OcrWord(String text, int x, int y, int width, int height, double confidence) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.confidence = confidence;
    }
    
    /**
     * @return the recognized text
     */
    public String getText() {
        return text;
    }
    
    /**
     * @return left coordinate of bounding box
     */
    public int getX() {
        return x;
    }
    
    /**
     * @return top coordinate of bounding box
     */
    public int getY() {
        return y;
    }
    
    /**
     * @return width of bounding box
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * @return height of bounding box
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * @return recognition confidence (0.0 = uncertain, 1.0 = certain)
     */
    public double getConfidence() {
        return confidence;
    }
    
    /**
     * @return center X coordinate (useful for clicking)
     */
    public int getCenterX() {
        return x + width / 2;
    }
    
    /**
     * @return center Y coordinate (useful for clicking)
     */
    public int getCenterY() {
        return y + height / 2;
    }
    
    /**
     * @return right edge coordinate (x + width)
     */
    public int getMaxX() {
        return x + width;
    }
    
    /**
     * @return bottom edge coordinate (y + height)
     */
    public int getMaxY() {
        return y + height;
    }
    
    /**
     * Checks if a point is inside this word's bounding box.
     * @param px point X
     * @param py point Y
     * @return true if point is inside
     */
    public boolean contains(int px, int py) {
        return px >= x && px <= getMaxX() && py >= y && py <= getMaxY();
    }
    
    @Override
    public String toString() {
        return String.format("OcrWord{text='%s', bounds=[%d,%d %dx%d], confidence=%.2f}",
            text, x, y, width, height, confidence);
    }
}
