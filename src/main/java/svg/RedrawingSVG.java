package svg;

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.concurrent.atomic.AtomicBoolean;

public class RedrawingSVG extends JFrame {

    protected AtomicBoolean shown = new AtomicBoolean(false);

    public static void main(String[] args) throws MalformedURLException, InterruptedException, FileNotFoundException, UnsupportedEncodingException, SVGGraphics2DIOException {
        RedrawingSVG redrawingSVG = new RedrawingSVG();
        redrawingSVG.drawSvg();
    }

    public void drawSvg() throws MalformedURLException, InterruptedException, FileNotFoundException, UnsupportedEncodingException, SVGGraphics2DIOException {
        final JSVGCanvas canvas = new JSVGCanvas();
        canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC); // to update it
        canvas.setURI(new File("/home/INTEXSOFT/eugene.kuntsevich/Downloads/img.svg").toURI().toURL().toString());

        canvas.addGVTTreeBuilderListener(new GVTTreeBuilderAdapter() {
            public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
                synchronized (shown) {
                    shown.set(true); // No modifications before!!
                    shown.notifyAll();
                }
            }
        });
        getContentPane().add(canvas);

        setSize(800, 400);
        setVisible(true);

        synchronized (shown) { // Strictly required to wait
            while (shown.get() == false) {
                try {
                    shown.wait(0);
                } catch (Exception e) {
                }
            }
        }
        Document doc = canvas.getSVGDocument();
        doc.setDocumentURI("http://www.w3.org/2000/svg");

        SVGGraphics2D g = new SVGGraphics2D(doc);
        Shape circle = new Ellipse2D.Double(50, 50, 1000, 1000);
        g.setPaint(Color.red);
        g.setStroke(new BasicStroke(25));
        g.draw(circle);

        Element root = doc.getDocumentElement();
        g.getRoot(root);

        /*Writer out;
        try {
            OutputStream outputStream = new FileOutputStream(new File("img2.svg"));
            out = new OutputStreamWriter(outputStream, "UTF-8");
            g.stream(out, true);
            outputStream.flush();
            outputStream.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SVGGraphics2DIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       // System.exit(0);*/
    }
}
