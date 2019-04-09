package svg;

import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.util.RunnableQueue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleBackSVG
        extends JFrame
{
    protected JSVGCanvas canvas;
    protected AtomicBoolean shown = new AtomicBoolean (false);

    public SimpleBackSVG() throws Exception {
        canvas = createCanvas ();
        getContentPane().add(canvas);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setSize(800, 400);
        setVisible(true);
    }

    public JSVGCanvas createCanvas() throws Exception {
        JSVGCanvas c = new JSVGCanvas();
        c.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC); // to update it
        c.setURI (new File ("/home/ekuntsevich/Downloads/img.svg").toURL().toString());

        // Set the JSVGCanvas listeners. This one is needed
        c.addGVTTreeBuilderListener(new GVTTreeBuilderAdapter() {
            public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
                synchronized (shown) {
                    shown.set (true); // No modifications before!!
                    shown.notifyAll();
                }
            }
        });
        return c;
    }

    public void changeBackground () throws Exception {
        synchronized (shown) { // Strictly required to wait
            while (shown.get() == false) {
                try {shown.wait (0);} catch (Exception e) {}
            }
        }
        Thread.currentThread().sleep (2000); // Just to see update
        UpdateManager updateManager = canvas.getUpdateManager();
        RunnableQueue rq = updateManager.getUpdateRunnableQueue();
        Runnable r = new Runnable () {public void run() {
            doChangeBackground ();
        }};
        rq.invokeAndWait(r); // Updates in this thread
    }

    public void doChangeBackground () {
        Document doc = canvas.getSVGDocument ();
        /*NodeList list = doc.getElementsByTagName("rect");
        for (int i=0; i<list.getLength(); i++) {
            Node node = list.item (i);
            SVGOMRectElement se = (SVGOMRectElement) node;
            se.getStyle().setProperty ("fill", "#000000", "");
        }*/
        SVGGraphics2D g = new SVGGraphics2D(doc);
        Shape circle = new Ellipse2D.Double(50, 50, 250, 250);
        g.setPaint(Color.red);
        g.draw(circle);

        Element root = doc.getDocumentElement();
        g.getRoot(root);
    }

    public static void main(String[] args) throws Exception{
        SimpleBackSVG app = new SimpleBackSVG();
        app.changeBackground();
    }
}
