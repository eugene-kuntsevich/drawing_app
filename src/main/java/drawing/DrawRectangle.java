package drawing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class DrawRectangle extends Canvas {

    public static void main(String[] args) {
        Canvas canvas = new DrawRectangle();

        canvas.setPreferredSize(new Dimension(600, 600));

        saveCanvas(canvas);
    }

    public static void saveCanvas(Canvas canvas) {
        BufferedImage image = new BufferedImage(canvas.getPreferredSize().width, canvas.getPreferredSize().height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = (Graphics2D) image.getGraphics();

        canvas.paint(g2);

        try {
            ImageIO.write(image, "png", new File("rectangle.png"));
        } catch (Exception e) {

        }
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setBackground(Color.BLACK);

        g2.setColor(Color.YELLOW);    // set the drawing color

        g2.setStroke(new BasicStroke(7)); //set thickness line

        g2.rotate(Math.toRadians(45), 100, 100);

        g2.drawRect(100, 100, 90, 50);
    }
}



