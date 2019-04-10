package svg;

import drawing.DrawText;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.concurrent.atomic.AtomicBoolean;

import static drawing.DrawArrow.drawArrow;

public class RedrawingSVG extends JFrame {

    protected AtomicBoolean shown = new AtomicBoolean(false);

    public static void main(String[] args) throws MalformedURLException {
        RedrawingSVG redrawingSVG = new RedrawingSVG();
        redrawingSVG.drawSvg();
    }

    public void drawSvg() throws MalformedURLException {
        final JSVGCanvas canvas = new JSVGCanvas();
        canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC); // to update it
        canvas.setURI(new File("/home/ekuntsevich/Downloads/img.svg").toURI().toURL().toString());

        canvas.addGVTTreeBuilderListener(new GVTTreeBuilderAdapter() {
            public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
                synchronized (shown) {
                    shown.set(true); // No modifications before!!
                    shown.notifyAll();
                }
            }
        });
        getContentPane().add(canvas);

        setSize(400, 200);

        synchronized (shown) { // Strictly required to wait
            while (shown.get() == false) {
                try {
                    shown.wait(0);
                } catch (Exception e) {
                }
            }
        }
        Document document = canvas.getSVGDocument();

        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
        drawArrow(svgGenerator, 25);
        DrawText drawText = new DrawText();
        drawText.paint(svgGenerator);

        Element root = document.getDocumentElement();
        svgGenerator.getRoot(root);

        Writer out;
        try {
            OutputStream outputStream = new FileOutputStream(new File("img2.svg"));
            out = new OutputStreamWriter(outputStream, "UTF-8");
            svgGenerator.stream(root, out);
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
    }
}
