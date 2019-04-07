package drawing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class DrawArrow {

    public static final int X1 = 20;
    public static final int Y1 = 50;
    public static final int X2 = 200;
    public static final int Y2 = 200;

    public static void main(String[] args) {
        BufferedImage image = new BufferedImage(800, 400, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = (Graphics2D) image.getGraphics();

        drawArrow(g2, 20);

        try {
            ImageIO.write(image, "png", new File("arrow.png"));
        } catch (Exception e) {

        }
    }

    private static void drawArrow(Graphics2D g2d, int thicknessLine) {
        AffineTransform tx = g2d.getTransform();
        Line2D.Double line = new Line2D.Double(X1, Y1, X2, Y2);

        Polygon arrowHead = new Polygon();
        int thicknessArrowHead = (int) (thicknessLine * 1.5);
        arrowHead.addPoint(0, thicknessArrowHead);
        arrowHead.addPoint(-thicknessArrowHead, -thicknessArrowHead);
        arrowHead.addPoint(thicknessArrowHead, -thicknessArrowHead);

        double angle = Math.atan2(line.y2 - line.y1, line.x2 - line.x1);
        tx.translate(line.x2, line.y2);
        tx.rotate((angle - Math.PI / 2d));

        Graphics2D g = (Graphics2D) g2d.create();
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(thicknessLine));  //set thickness line
        g.drawLine(X1, Y1, X2, Y2);
        g.setTransform(tx);
        g.fill(arrowHead);
        g.dispose();
    }
}



