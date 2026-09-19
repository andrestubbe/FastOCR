package fastocr.demo;

import fastocr.FastOCR;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;

public class Demo {
    public static void main(String[] args) {
        System.out.println("--- FastOCR 0.1.2 Demo ---");
        try {
            FastOCR ocr = new FastOCR("en");
            BufferedImage image = new BufferedImage(1200, 180, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
            graphics.setColor(Color.BLACK);
            graphics.setFont(new Font("Segoe UI", Font.BOLD, 42));
            graphics.drawString("FastOCR 0.1.2 native OCR demo", 40, 90);
            graphics.dispose();

            String text = ocr.read(image);
            if (text == null || text.isBlank()) {
                throw new IllegalStateException("OCR returned no text");
            }
            System.out.println("[+] Recognized: " + text.trim());
            speak(text.trim());
            ocr.close();
        } catch (Exception e) {
            System.err.println("[ERROR] FastOCR demo failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("✔ FastOCR demo completed.");
    }

    private static void speak(String text) throws Exception {
        Path textFile = Files.createTempFile("fastocr-demo-", ".txt");
        try {
            Files.writeString(textFile, text);
            String path = textFile.toAbsolutePath().toString().replace("'", "''");
            String command = "$text = [System.IO.File]::ReadAllText('" + path + "', [System.Text.Encoding]::UTF8); " +
                    "Add-Type -AssemblyName System.Speech; " +
                    "$synth = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                    "$synth.Speak($text); $synth.Dispose()";
            Process process = new ProcessBuilder("powershell.exe", "-NoProfile", "-NonInteractive", "-Command", command)
                    .inheritIO()
                    .start();
            if (process.waitFor() != 0) {
                throw new IllegalStateException("Windows SAPI returned exit code " + process.exitValue());
            }
        } finally {
            Files.deleteIfExists(textFile);
        }
    }
}
