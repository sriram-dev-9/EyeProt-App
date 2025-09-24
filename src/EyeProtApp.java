import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;


public class EyeProtApp {
    private static final int WORK_INTERVAL = 20 * 60 * 1000; 
    private static final int REST_DURATION = 20 * 1000;      
    
    private SystemTray systemTray;
    private TrayIcon trayIcon;
    private Timer workTimer;
    private JWindow notificationWindow;
    private JPanel notificationPanel;
    private JLabel countdownLabel;
    private boolean isResting = false;
    private int secondsLeft = 20;
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                EyeProtApp app = new EyeProtApp();
                app.initialize();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error initializing application: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
    
   
    private void initialize() throws AWTException {
        if (!SystemTray.isSupported()) {
            throw new UnsupportedOperationException("System tray is not supported on this platform");
        }
        
        setupSystemTray();
        
        createNotificationWindow();
        
        startWorkTimer();
        
        trayIcon.displayMessage("EyeProt", 
                "EyeProt is running in the system tray.\nIt will remind you to follow the 20-20-20 rule.", 
                TrayIcon.MessageType.INFO);
    }
    
  
    private void setupSystemTray() throws AWTException {
        systemTray = SystemTray.getSystemTray();
        
        Image trayIconImage = createTrayIconImage();
        
        PopupMenu popupMenu = new PopupMenu();
        
        MenuItem startItem = new MenuItem("Start Timer");
        startItem.addActionListener(e -> startWorkTimer());
        
        MenuItem pauseItem = new MenuItem("Pause Timer");
        pauseItem.addActionListener(e -> pauseTimer());
        
        MenuItem testItem = new MenuItem("Test Notification");
        testItem.addActionListener(e -> showRestNotification());
        
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(null,
                    "EyeProt - 20-20-20 Rule Eye Protection\n\n" +
                    "Every 20 minutes, look at something 20 feet away for 20 seconds.\n\n" +
                    "This application helps reduce eye strain by reminding you to\n" +
                    "follow the 20-20-20 rule with non-intrusive notifications.",
                    "About EyeProt",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> {
            cleanupAndExit();
        });
        
        popupMenu.add(startItem);
        popupMenu.add(pauseItem);
        popupMenu.add(testItem);
        popupMenu.addSeparator();
        popupMenu.add(aboutItem);
        popupMenu.addSeparator();
        popupMenu.add(exitItem);
        
        trayIcon = new TrayIcon(trayIconImage, "EyeProt - 20-20-20 Rule", popupMenu);
        trayIcon.setImageAutoSize(true);
        
        trayIcon.addActionListener(e -> {
            if (isResting) {
                trayIcon.displayMessage("EyeProt", 
                    "Rest time! Look 20 feet away for " + secondsLeft + " more seconds.", 
                    TrayIcon.MessageType.INFO);
            } else {
                trayIcon.displayMessage("EyeProt", 
                    "Next break in " + formatTimeRemaining(), 
                    TrayIcon.MessageType.INFO);
            }
        });
        
        systemTray.add(trayIcon);
    }
    
    private Image createTrayIconImage() {
        int size = 16; 
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(Color.BLACK);
        g2d.fillOval(0, 0, size - 1, size - 1);
        
        g2d.setColor(Color.BLUE);
        g2d.fillOval(4, 4, size - 9, size - 9);
        
        g2d.setColor(Color.BLACK);
        g2d.fillOval(6, 6, 4, 4);
        
        g2d.dispose();
        return image;
    }

    private void cleanupAndExit() {
        if (workTimer != null) {
            workTimer.cancel();
            workTimer = null;
        }
        
        if (systemTray != null && trayIcon != null) {
            systemTray.remove(trayIcon);
        }
        
        if (notificationWindow != null) {
            notificationWindow.dispose();
        }
        
        System.exit(0);
    }
    
 
    private void startWorkTimer() {
        pauseTimer();
        
        workTimer = new Timer();
        workTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                showRestNotification();
            }
        }, WORK_INTERVAL, WORK_INTERVAL);
        
        trayIcon.displayMessage("EyeProt", 
                "Timer started. Next break in 20 minutes.", 
                TrayIcon.MessageType.INFO);
    }
    
    
    private void pauseTimer() {
        if (workTimer != null) {
            workTimer.cancel();
            workTimer = null;
            
            if (notificationWindow != null && notificationWindow.isVisible()) {
                notificationWindow.setVisible(false);
            }
            
            isResting = false;
            
            trayIcon.displayMessage("EyeProt", "Timer paused.", TrayIcon.MessageType.INFO);
        }
    }
    
 
    private void showRestNotification() {
        SwingUtilities.invokeLater(() -> {
            if (notificationWindow != null) {
                secondsLeft = REST_DURATION / 1000;
                updateCountdownLabel();
                
                if (!notificationWindow.isVisible()) {
                    notificationWindow.setVisible(true);
                    isResting = true;
                }
                
                Timer countdownTimer = new Timer();
                countdownTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (secondsLeft <= 0) {
                            SwingUtilities.invokeLater(() -> {
                                notificationWindow.setVisible(false);
                                isResting = false;
                            });
                            cancel();
                            return;
                        }
                        
                        secondsLeft--;
                        
                        SwingUtilities.invokeLater(() -> updateCountdownLabel());
                    }
                }, 1000, 1000);
            }
        });
    }
    

    private void updateCountdownLabel() {
        if (countdownLabel != null) {
            countdownLabel.setText("Look away for " + secondsLeft + " seconds");
        }
    }
    
  
    private String formatTimeRemaining() {
        // Calculate time until next notification
        if (workTimer == null) {
            return "Timer paused";
        }
        
        return "about 20 minutes";
    }
    
    private void createNotificationWindow() {
        notificationWindow = new JWindow();
        
        notificationWindow.setBackground(new Color(0, 0, 0, 0));
        
        notificationPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (g instanceof Graphics2D) {
                    final int R = 0;
                    final int G = 0;
                    final int B = 100;
                    final int ALPHA = 60; 
                    
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    
                    g2d.setColor(new Color(R, G, B, ALPHA));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    
                    g2d.setColor(new Color(0, 0, 200, 100));
                    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                }
            }
        };
        
        notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));
        notificationPanel.setOpaque(false);
        notificationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Take an Eye Break!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel instructionLabel = new JLabel("Look at an object 20 feet (~6 meters) away");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        countdownLabel = new JLabel("Look away for 20 seconds");
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 18));
        countdownLabel.setForeground(Color.WHITE);
        countdownLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel iconLabel = new JLabel(createEyeIcon());
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton dismissButton = new JButton("Dismiss");
        dismissButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        dismissButton.setFocusPainted(false);
        dismissButton.addActionListener(e -> {
            notificationWindow.setVisible(false);
            isResting = false;
        });
        
        notificationPanel.add(iconLabel);
        notificationPanel.add(Box.createVerticalStrut(15));
        notificationPanel.add(titleLabel);
        notificationPanel.add(Box.createVerticalStrut(10));
        notificationPanel.add(instructionLabel);
        notificationPanel.add(Box.createVerticalStrut(20));
        notificationPanel.add(countdownLabel);
        notificationPanel.add(Box.createVerticalStrut(20));
        notificationPanel.add(dismissButton);
        
        notificationWindow.add(notificationPanel);
        
        notificationWindow.setSize(400, 300);
        centerWindowOnScreen(notificationWindow);
        
        notificationWindow.setFocusableWindowState(false);
        
        notificationWindow.setVisible(false);
    }
    
   
    private ImageIcon createEyeIcon() {
        int size = 64;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(Color.WHITE);
        g2d.fillOval(0, 0, size - 1, size / 2);
        
        g2d.setColor(new Color(30, 144, 255));
        int irisSize = size / 3;
        g2d.fillOval((size - irisSize) / 2, (size / 4) - (irisSize / 2), irisSize, irisSize);
        
        g2d.setColor(Color.BLACK);
        int pupilSize = irisSize / 2;
        g2d.fillOval((size - pupilSize) / 2, (size / 4) - (pupilSize / 2), pupilSize, pupilSize);
        
        g2d.setStroke(new BasicStroke(2f));
        g2d.setColor(Color.BLACK);
        g2d.drawArc(0, 0, size - 1, size / 2, 0, 180);
        g2d.drawArc(0, 0, size - 1, size / 2, 180, 180);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    

    private void centerWindowOnScreen(Window window) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        
        int x = (int) (rect.getMaxX() - window.getWidth()) / 2;
        int y = (int) (rect.getMaxY() - window.getHeight()) / 2;
        window.setLocation(x, y);
    }
}
