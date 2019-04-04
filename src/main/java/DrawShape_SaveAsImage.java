import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class DrawShape_SaveAsImage extends Canvas {

    public static void main(String[] args) {
        Frame f = new Frame("Draw shape and text on Canvas");
        final Canvas canvas = new DrawShape_SaveAsImage();

        f.add(canvas);

        f.setSize(600, 600);
        f.setVisible(true);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                saveCanvas(canvas);
                System.exit(0);
            }
        });

    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setBackground(Color.BLACK);
        g2.setColor(Color.YELLOW);    // set the drawing color
        g2.drawLine(30, 40, 100, 200);
        g2.drawOval(150, 180, 10, 10);
        //g2.rotate(45, 300, 300);
        g2.drawRect(200, 210, 20, 30);

        g2.setColor(Color.RED);       // change the drawing color
        g2.fillOval(300, 310, 30, 50);
        g2.fillRect(400, 350, 60, 50);

        // Printing texts
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.ITALIC, 12));
        g2.drawString("Testing custom drawing ...", 10, 20);
    }

    public static void saveCanvas(Canvas canvas) {

        BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = (Graphics2D) image.getGraphics();


        canvas.paint(g2);
        try {
            ImageIO.write(image, "png", new File("canvas.png"));
        } catch (Exception e) {

        }
    }
}



