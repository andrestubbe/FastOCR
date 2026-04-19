package fastocr;

import java.util.Collections;
import java.util.List;

/**
 * Complete OCR result with lines, words, and text.
 * 
 * <p>This is the detailed result from Windows.Media.Ocr containing all
 * recognized text with spatial information. Use this when you need:
 * <ul>
 *   <li>Clickable text locations</li>
 *   <li>Text highlighting/marking</li>
 *   <li>Form field detection</li>
 *   <li>Confidence thresholds</li>
 * </ul></p>
 * 
 * <p><b>Structure:</b></p>
 * <pre>
 * OcrResult
 * ├── getText() → "Hello World\nSecond Line"
 * ├── getLines()
 * │   ├── OcrLine[0]: "Hello World"
 * │   │   ├── getWords()
 * │   │   │   ├── OcrWord[0]: "Hello" at (10, 20, 50, 30)
 * │   │   │   └── OcrWord[1]: "World" at (70, 20, 60, 30)
 * │   └── OcrLine[1]: "Second Line"
 * └── getWords() (flattened: all words in reading order)
 * </pre>
 * 
 * @author FastJava Team
 * @version 1.0.0
 * @since 1.0.0
 * @see <a href="https://github.com/andrestubbe/FastJava">FastJava Ecosystem</a>
 */
public class OcrResult {
    
    private final String text;
    private final List<OcrLine> lines;
    private final List<OcrWord> allWords;
    
    /**
     * Creates an OCR result.
     * 
     * @param text full recognized text (lines joined with \n)
     * @param lines list of lines in reading order (top-to-bottom)
     */
    public OcrResult(String text, List<OcrLine> lines) {
        this.text = text;
        this.lines = Collections.unmodifiableList(lines);
        
        // Flatten all words from all lines
        this.allWords = lines.stream()
            .flatMap(line -> line.getWords().stream())
            .toList();
    }
    
    /**
     * @return full recognized text with newlines between lines
     */
    public String getText() {
        return text;
    }
    
    /**
     * @return unmodifiable list of lines in reading order
     */
    public List<OcrLine> getLines() {
        return lines;
    }
    
    /**
     * @return total number of lines
     */
    public int getLineCount() {
        return lines.size();
    }
    
    /**
     * @return all words from all lines in reading order
     */
    public List<OcrWord> getWords() {
        return allWords;
    }
    
    /**
     * @return total number of words
     */
    public int getWordCount() {
        return allWords.size();
    }
    
    /**
     * Finds a word at the specified point (useful for clicking).
     * 
     * @param x point X coordinate
     * @param y point Y coordinate
     * @return the word at that point, or null if none
     */
    public OcrWord getWordAt(int x, int y) {
        for (OcrWord word : allWords) {
            if (word.contains(x, y)) {
                return word;
            }
        }
        return null;
    }
    
    /**
     * Finds text containing the search string (case-insensitive).
     * 
     * @param search text to search for
     * @return first matching word, or null
     */
    public OcrWord findWord(String search) {
        String lowerSearch = search.toLowerCase();
        for (OcrWord word : allWords) {
            if (word.getText().toLowerCase().contains(lowerSearch)) {
                return word;
            }
        }
        return null;
    }
    
    /**
     * Finds the first word with confidence above threshold.
     * 
     * @param minConfidence minimum confidence (0.0-1.0)
     * @return first high-confidence word, or null
     */
    public OcrWord findHighConfidenceWord(double minConfidence) {
        for (OcrWord word : allWords) {
            if (word.getConfidence() >= minConfidence) {
                return word;
            }
        }
        return null;
    }
    
    /**
     * Gets average confidence of all words.
     * @return average confidence (0.0-1.0)
     */
    public double getAverageConfidence() {
        if (allWords.isEmpty()) return 0.0;
        return allWords.stream()
            .mapToDouble(OcrWord::getConfidence)
            .average()
            .orElse(0.0);
    }
    
    /**
     * Gets bounding box of entire result.
     * @return [minX, minY, maxX, maxY]
     */
    public int[] getBoundingBox() {
        if (allWords.isEmpty()) return new int[]{0, 0, 0, 0};
        
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        
        for (OcrWord word : allWords) {
            minX = Math.min(minX, word.getX());
            minY = Math.min(minY, word.getY());
            maxX = Math.max(maxX, word.getMaxX());
            maxY = Math.max(maxY, word.getMaxY());
        }
        
        return new int[]{minX, minY, maxX, maxY};
    }
    
    @Override
    public String toString() {
        return String.format("OcrResult{lines=%d, words=%d, text='%s...'}",
            lines.size(), allWords.size(), 
            text.length() > 20 ? text.substring(0, 20) : text);
    }
}
