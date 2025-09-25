import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class CreateIcon {
    public static void main(String[] args) {
        try {
            // Create a large icon (256x256) for better quality
            int size = 256;
            BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Background - transparent
            g2d.setColor(new Color(0, 0, 0, 0));
            g2d.fillRect(0, 0, size, size);
            
            // Eye outline (white)
            g2d.setColor(Color.WHITE);
            g2d.fillOval(size/10, size/4, size*8/10, size/2);
            
            // Blue iris
            g2d.setColor(new Color(30, 144, 255));
            int irisSize = size / 3;
            g2d.fillOval((size - irisSize) / 2, (size / 4) - (irisSize / 6) + size/6, irisSize, irisSize);
            
            // Black pupil
            g2d.setColor(Color.BLACK);
            int pupilSize = irisSize / 2;
            g2d.fillOval((size - pupilSize) / 2, (size / 4) - (pupilSize / 6) + size/6, pupilSize, pupilSize);
            
            // Eye outline
            g2d.setStroke(new BasicStroke(4f));
            g2d.setColor(Color.BLACK);
            g2d.drawArc(size/10, size/4, size*8/10, size/2, 0, 180);
            g2d.drawArc(size/10, size/4, size*8/10, size/2, 180, 180);
            
            g2d.dispose();
            
            // Save the icon as a PNG
            File outputFile = new File("resources/icon.png");
            ImageIO.write(image, "png", outputFile);
            
            System.out.println("Icon created successfully: " + outputFile.getAbsolutePath());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

