package fastocr;

import java.util.Collections;
import java.util.List;

/**
 * Represents a line of recognized text containing multiple words.
 * 
 * <p>Windows.Media.Ocr groups words into lines based on their spatial layout.
 * This preserves reading order and paragraph structure.</p>
 * 
 * @author FastJava Team
 * @version 1.0.0
 * @since 1.0.0
 * @see <a href="https://github.com/andrestubbe/FastJava">FastJava Ecosystem</a>
 */
public class OcrLine {
    
    private final String text;
    private final List<OcrWord> words;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    
    /**
     * Creates an OCR line result.
     * 
     * @param text the full line text (concatenated words)
     * @param words list of words in this line
     * @param x left coordinate of bounding box
     * @param y top coordinate of bounding box
     * @param width width of bounding box
     * @param height height of bounding box
     */
    public OcrLine(String text, List<OcrWord> words, int x, int y, int width, int height) {
        this.text = text;
        this.words = Collections.unmodifiableList(words);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    /**
     * @return the full line text (words concatenated with spaces)
     */
    public String getText() {
        return text;
    }
    
    /**
     * @return unmodifiable list of words in this line (in reading order)
     */
    public List<OcrWord> getWords() {
        return words;
    }
    
    /**
     * @return number of words in this line
     */
    public int getWordCount() {
        return words.size();
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
     * @return center X coordinate
     */
    public int getCenterX() {
        return x + width / 2;
    }
    
    /**
     * @return center Y coordinate
     */
    public int getCenterY() {
        return y + height / 2;
    }
    
    /**
     * Finds a word at the specified point.
     * @param px point X
     * @param py point Y  
     * @return the word at that point, or null
     */
    public OcrWord getWordAt(int px, int py) {
        for (OcrWord word : words) {
            if (word.contains(px, py)) {
                return word;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return String.format("OcrLine{text='%s', words=%d, bounds=[%d,%d %dx%d]}",
            text, words.size(), x, y, width, height);
    }
}
