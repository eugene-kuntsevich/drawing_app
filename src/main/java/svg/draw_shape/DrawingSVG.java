package svg.draw_shape;

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.text.AttributedString;
import java.util.concurrent.atomic.AtomicBoolean;

public class DrawingSVG {
    public static void main(String[] args) throws IOException, InterruptedException {
        String path = "/home/INTEXSOFT/eugene.kuntsevich/Documents/projects/drawing_app/img.svg";

        SVGGraphics2D svgGenerator = getSvgGenerator(path);

//      example drawing RECTANGLE
        drawRectangleSVG(svgGenerator, 600, 200, 400, 400, 20, false, 45, Color.GREEN);

//      example drawing TEXT
        String textForDrawing = "Testing custom drawing ...";

        AttributedString as = new AttributedString(textForDrawing);
        as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 0, textForDrawing.length());
        as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON, 0, textForDrawing.length());

        svgGenerator.setFont(new Font("Bold", Font.BOLD, 80));

        drawTextSVG(svgGenerator, 100, 800, as, 0, Color.RED);

//      example drawing OVAL
        drawOvalSVG(svgGenerator, 600, 200, 200, 70, 20, false, 0, Color.BLUE);

//      example drawing ARROW
        drawArrowSVG(svgGenerator, 400, 400, 600, 200, 20, Color.RED);

        saveSvgImage(path, svgGenerator);
    }

    private static void drawArrowSVG(SVGGraphics2D svgGenerator, int coordX1, int coordY1, int coordX2, int coordY2, int thickness, Color color) {
        Polygon arrowHead = new Polygon();
        int sizeArrowHead = (int) (thickness * 1.5);

        arrowHead.addPoint(0, sizeArrowHead);
        arrowHead.addPoint(-sizeArrowHead, -sizeArrowHead);
        arrowHead.addPoint(sizeArrowHead, -sizeArrowHead);

        double angle = Math.atan2(coordY2 - coordY1, coordX2 - coordX1);

        AffineTransform tx = svgGenerator.getTransform();
        tx.translate(coordX2, coordY2);
        tx.rotate((angle - Math.PI / 2d));

        svgGenerator.setColor(color);                                   //set the drawing color
        svgGenerator.setStroke(new BasicStroke(thickness));             //set thickness line
        svgGenerator.drawLine(coordX1, coordY1, coordX2, coordY2);
        svgGenerator.setTransform(tx);
        svgGenerator.fill(arrowHead);
    }

    private static void drawOvalSVG(SVGGraphics2D svgGenerator, int coordX, int coordY, int radiusX, int radiusY, int thickness, boolean isFilled, int angle, Color color) {
        svgGenerator.setColor(color);                                  //set the drawing color

        svgGenerator.rotate(Math.toRadians(angle), coordX, coordY);    //set angle

        if (isFilled) {
            svgGenerator.fillOval(coordX, coordY, radiusX, radiusY);
        } else {
            svgGenerator.setStroke(new BasicStroke(thickness));        //set thickness line
            svgGenerator.drawOval(coordX, coordY, radiusX, radiusY);
        }

        svgGenerator.rotate(Math.toRadians(-angle), coordX, coordY);   //set canvas in initial position (with angle equal 0)
    }


    private static void drawRectangleSVG(SVGGraphics2D svgGenerator, int coordX, int coordY, int height, int width, int thickness, boolean isFilled, int angle, Color color) {
        svgGenerator.setColor(color);                                  // set the drawing color

        svgGenerator.rotate(Math.toRadians(angle), coordX, coordY);    //set angle

        if (isFilled) {
            svgGenerator.fillRect(coordX, coordY, width, height);
        } else {
            svgGenerator.setStroke(new BasicStroke(thickness));        //set thickness line
            svgGenerator.drawRect(coordX, coordY, width, height);
        }

        svgGenerator.rotate(Math.toRadians(-angle), coordX, coordY);   //set canvas in initial position (with angle equal 0)
    }

    private static void drawTextSVG(SVGGraphics2D svgGenerator, int coordX, int coordY, AttributedString as, int angle, Color color) {
        svgGenerator.setColor(color);

        svgGenerator.rotate(Math.toRadians(angle), coordX, coordY);    //set angle

        svgGenerator.drawString(as.getIterator(), coordX, coordY);

        svgGenerator.rotate(Math.toRadians(-angle), coordX, coordY);   //set canvas in initial position (with angle equal 0)
    }

    public static SVGGraphics2D getSvgGenerator(String path) throws InterruptedException, MalformedURLException {
        final JSVGCanvas canvas = new JSVGCanvas();
        canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC); // to update it
        canvas.setURI(new File(path).toURI().toURL().toString());

        final AtomicBoolean shown = new AtomicBoolean(false);
        canvas.addGVTTreeBuilderListener(new GVTTreeBuilderAdapter() {
            public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
                synchronized (shown) {
                    shown.set(true);
                    shown.notifyAll();
                }
            }
        });

        synchronized (shown) {
            while (!shown.get()) {
                shown.wait(0);
            }
        }

        Document document = canvas.getSVGDocument();

        return new SVGGraphics2D(document);
    }

    public static void saveSvgImage(String path, SVGGraphics2D svgGenerator) throws IOException {
        Element root = svgGenerator.getDOMFactory().getDocumentElement();
        svgGenerator.getRoot(root);

        OutputStream outputStream = new FileOutputStream(new File(path));
        Writer out = new OutputStreamWriter(outputStream, "UTF-8");
        svgGenerator.stream(root, out);
        outputStream.flush();
        outputStream.close();
    }
}
