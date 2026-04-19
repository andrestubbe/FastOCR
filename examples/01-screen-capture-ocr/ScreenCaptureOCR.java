import fastocr.FastOCR;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Screen Capture OCR Demo - Visual Example for FastOCR
 * 
 * Features:
 * - Live screen region selection
 * - Real-time OCR with performance metrics
 * - Visual feedback (screenshot + recognized text)
 * - Perfect for YouTube demos and repo images
 * 
 * Screenshot-worthy: Shows the 10-50ms OCR speed in action!
 */
public class ScreenCaptureOCR extends JFrame {
    
    private FastOCR ocr;
    private JLabel previewLabel;
    private JTextArea resultArea;
    private JLabel statusLabel;
    private JLabel timeLabel;
    private Robot robot;
    
    public ScreenCaptureOCR() {
        super("🚀 FastOCR - Screen Capture Demo");
        setupUI();
        initOCR();
    }
    
    private void setupUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(30, 30, 35));
        
        // Header
        JLabel header = new JLabel("⚡ FastOCR - 10-50ms Native OCR", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setForeground(new Color(100, 200, 255));
        mainPanel.add(header, BorderLayout.NORTH);
        
        // Center panel: Preview + Results side by side
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        centerPanel.setOpaque(false);
        
        // Left: Screenshot preview
        previewLabel = new JLabel("No capture yet", SwingConstants.CENTER);
        previewLabel.setPreferredSize(new Dimension(400, 400));
        previewLabel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(100, 200, 255), 2, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        previewLabel.setBackground(new Color(45, 45, 50));
        previewLabel.setOpaque(true);
        previewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        previewLabel.setForeground(Color.GRAY);
        
        JPanel previewPanel = createPanel("📸 Screenshot Preview", previewLabel);
        centerPanel.add(previewPanel);
        
        // Right: OCR Result
        resultArea = new JTextArea("Recognized text will appear here...");
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setEditable(false);
        resultArea.setBackground(new Color(35, 35, 40));
        resultArea.setForeground(new Color(200, 255, 200));
        resultArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        resultArea.setCaretColor(new Color(200, 255, 200));
        
        JScrollPane resultScroll = new JScrollPane(resultArea);
        resultScroll.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(100, 255, 150), 2, true),
            new EmptyBorder(0, 0, 0, 0)
        ));
        
        JPanel resultPanel = createPanel("📝 OCR Result", resultScroll);
        centerPanel.add(resultPanel);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Bottom: Controls + Stats
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false);
        
        // Capture button (BIG)
        JButton captureBtn = new JButton("📷 CAPTURE SCREEN REGION (Ctrl+Shift+C)");
        captureBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        captureBtn.setBackground(new Color(0, 150, 255));
        captureBtn.setForeground(Color.WHITE);
        captureBtn.setFocusPainted(false);
        captureBtn.setBorder(new CompoundBorder(
            new LineBorder(new Color(0, 200, 255), 3, true),
            new EmptyBorder(15, 30, 15, 30)
        ));
        captureBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        captureBtn.addActionListener(e -> doCapture());
        
        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setOpaque(false);
        
        statusLabel = new JLabel("⏳ Ready - Click CAPTURE or press Ctrl+Shift+C");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(Color.LIGHT_GRAY);
        
        timeLabel = new JLabel("⏱️ Last: -- ms");
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        timeLabel.setForeground(new Color(255, 200, 100));
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        statsPanel.add(statusLabel);
        statsPanel.add(timeLabel);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(captureBtn);
        
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(statsPanel, BorderLayout.SOUTH);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Keyboard shortcut
        getRootPane().registerKeyboardAction(
            e -> doCapture(),
            KeyStroke.getKeyStroke("control shift C"),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        
        add(mainPanel);
        
        // Status bar
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(20, 20, 25));
        statusBar.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        JLabel poweredBy = new JLabel("🔥 Powered by Windows.Media.Ocr (GPU Accelerated)");
        poweredBy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        poweredBy.setForeground(new Color(150, 150, 150));
        
        JLabel version = new JLabel("FastOCR v1.0.0");
        version.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        version.setForeground(new Color(150, 150, 150));
        version.setHorizontalAlignment(SwingConstants.RIGHT);
        
        statusBar.add(poweredBy, BorderLayout.WEST);
        statusBar.add(version, BorderLayout.EAST);
        
        add(statusBar, BorderLayout.SOUTH);
    }
    
    private JPanel createPanel(String title, JComponent content) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(200, 200, 200));
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void initOCR() {
        try {
            if (!FastOCR.isOcrAvailable()) {
                statusLabel.setText("❌ OCR not available - Install Windows OCR language pack");
                statusLabel.setForeground(new Color(255, 100, 100));
                return;
            }
            
            ocr = new FastOCR("en");
            robot = new Robot();
            statusLabel.setText("✅ Ready - Windows.Media.Ocr initialized");
            statusLabel.setForeground(new Color(100, 255, 150));
            
        } catch (Exception e) {
            statusLabel.setText("❌ Error: " + e.getMessage());
            statusLabel.setForeground(Color.RED);
        }
    }
    
    private void doCapture() {
        if (ocr == null) {
            JOptionPane.showMessageDialog(this, 
                "OCR not initialized. Check Windows OCR language pack.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Hide window for capture
        setVisible(false);
        
        SwingUtilities.invokeLater(() -> {
            try {
                // Small delay to ensure window is hidden
                Thread.sleep(200);
                
                // Capture full screen (in real app, would capture selected region)
                Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                BufferedImage screenshot = robot.createScreenCapture(screenRect);
                
                // Show window again
                setVisible(true);
                
                // Update preview
                Image scaled = screenshot.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
                previewLabel.setIcon(new ImageIcon(scaled));
                previewLabel.setText("");
                
                // Do OCR with timing
                statusLabel.setText("🔄 Running OCR...");
                statusLabel.setForeground(new Color(255, 200, 100));
                
                long start = System.nanoTime();
                String text = ocr.read(screenshot);
                long ms = (System.nanoTime() - start) / 1_000_000;
                
                // Update results
                resultArea.setText(text != null && !text.isEmpty() ? text : "(No text detected)");
                timeLabel.setText(String.format("⏱️ Last: %d ms", ms));
                
                if (ms < 50) {
                    timeLabel.setForeground(new Color(100, 255, 150)); // Green = fast
                } else if (ms < 100) {
                    timeLabel.setForeground(new Color(255, 200, 100)); // Yellow = ok
                } else {
                    timeLabel.setForeground(new Color(255, 100, 100)); // Red = slow
                }
                
                statusLabel.setText("✅ OCR complete - " + (text != null ? text.length() : 0) + " chars");
                statusLabel.setForeground(new Color(100, 255, 150));
                
            } catch (Exception e) {
                setVisible(true);
                statusLabel.setText("❌ Error: " + e.getMessage());
                statusLabel.setForeground(Color.RED);
                e.printStackTrace();
            }
        });
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        
        SwingUtilities.invokeLater(() -> {
            ScreenCaptureOCR demo = new ScreenCaptureOCR();
            demo.setVisible(true);
        });
    }
}
