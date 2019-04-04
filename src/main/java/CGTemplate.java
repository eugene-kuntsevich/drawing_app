import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** Custom Drawing Code Template */
// A Swing application extends javax.swing.JFrame
public class CGTemplate extends JFrame {
    // Define constants
    public static final int CANVAS_WIDTH  = 640;
    public static final int CANVAS_HEIGHT = 480;

    // Declare an instance of the drawing canvas,
    // which is an inner class called DrawCanvas extending javax.swing.JPanel.
    private DrawCanvas canvas;

    // Constructor to set up the GUI components and event handlers
    public CGTemplate() throws IOException {
        canvas = new DrawCanvas();    // Construct the drawing canvas
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        // Set the Drawing JPanel as the JFrame's content-pane
        Container cp = getContentPane();
        cp.add(canvas);
        // or "setContentPane(canvas);"

        BufferedImage image=new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT,BufferedImage.TYPE_INT_RGB);

        Graphics2D g2=(Graphics2D)image.getGraphics();


        canvas.paint(g2);
        try {
            ImageIO.write(image, "png", new File("canvas.png"));
        } catch (Exception e) {

        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);   // Handle the CLOSE button
        pack();              // Either pack() the components; or setSize()
        setTitle("......");  // "super" JFrame sets the title
        setVisible(true);    // "super" JFrame show
    }

    /**
     * Define inner class DrawCanvas, which is a JPanel used for custom drawing.
     */
    private class DrawCanvas extends JPanel {
        // Override paintComponent to perform your own painting
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);     // paint parent's background
            setBackground(Color.BLACK);  // set background color for this JPanel

            // Your custom painting codes. For example,
            // Drawing primitive shapes
            g.setColor(Color.YELLOW);    // set the drawing color
            g.drawLine(30, 40, 100, 200);
            g.drawOval(150, 180, 10, 10);
            g.drawRect(200, 210, 20, 30);
            g.setColor(Color.RED);       // change the drawing color
            g.fillOval(300, 310, 30, 50);
            g.fillRect(400, 350, 60, 50);
            // Printing texts
            g.setColor(Color.WHITE);
            g.setFont(new Font("Monospaced", Font.PLAIN, 12));
            g.drawString("Testing custom drawing ...", 10, 20);
        }
    }

    // The entry main method
    public static void main(String[] args) {
        // Run the GUI codes on the Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new CGTemplate(); // Let the constructor do the job
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}