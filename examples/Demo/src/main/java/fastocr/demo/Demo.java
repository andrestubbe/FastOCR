package fastocr.demo;

import fastocr.FastOCR;

public class Demo {
    public static void main(String[] args) {
        System.out.println("--- FastOCR 0.1.1 Demo ---");
        try {
            FastOCR ocr = new FastOCR("en");
            System.out.println("[+] FastOCR Native Engine initialized for language 'en'.");
            ocr.close();
        } catch (Exception e) {
            System.out.println("Native OCR Note: " + e.getMessage());
        }
        System.out.println("✔ FastOCR demo completed.");
    }
}
