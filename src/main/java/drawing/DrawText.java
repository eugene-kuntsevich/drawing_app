package drawing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;

public class DrawText extends Canvas {

    public static void main(String[] args) {
        Canvas canvas = new DrawText();

        canvas.setPreferredSize(new Dimension(600, 600));

        saveCanvas(canvas);
    }

    public static void saveCanvas(Canvas canvas) {
        BufferedImage image = new BufferedImage(canvas.getPreferredSize().width, canvas.getPreferredSize().height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = (Graphics2D) image.getGraphics();

        canvas.paint(g2);

        try {
            ImageIO.write(image, "png", new File("text.png"));
        } catch (Exception e) {

        }
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setBackground(Color.BLACK);

        g2.setColor(Color.YELLOW);    // set the drawing color

        // Printing plain text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Plain", Font.PLAIN, 24));
        g2.drawString("Testing custom drawing ...", 10, 20);

        // Printing bold text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Bold", Font.BOLD, 24));
        g2.drawString("Testing custom drawing ...", 10, 50);

        // Printing UNDERLINE text
        String s = "Testing custom drawing ...";
        Font plainFont = new Font("Times New Roman", Font.PLAIN, 24);

        AttributedString as = new AttributedString(s);
        as.addAttribute(TextAttribute.FONT, plainFont);
        as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 0, s.length());

        g2.drawString(as.getIterator(), 10, 80);

        // Printing STRIKETHROUGH text with 'pacifico' font
        String s2 = "Testing custom drawing ...";
        Font pacificoFont = null;

        try {
            pacificoFont = Font.createFont(Font.TRUETYPE_FONT, new File("Pacifico.ttf")).deriveFont(30f);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AttributedString as2 = new AttributedString(s2);
        as2.addAttribute(TextAttribute.FONT, pacificoFont);
        as2.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON, 0, s.length());

        g2.drawString(as2.getIterator(), 10, 120);
    }
}



