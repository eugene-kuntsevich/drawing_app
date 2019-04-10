package svg;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;

public class UsingBatikWithExistingDoc {
    String parser = XMLResourceDescriptor.getXMLParserClassName();
    SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
    String filename = "D:\\test\\old.svg";
    File file = new File(filename);
    String uri = file.toURI().toString();
    Document doc = f.createDocument(uri);

    public UsingBatikWithExistingDoc() throws IOException {
    }

    //manipulate
    /*SVGGraphics2D generator = new SVGGraphics2D(doc);
    Shape circle = new Ellipse2D.Double(0, 0, 50, 50);
    generator.
    g.setStroke

    //append the generator to the existing svg file
   generator.getRoot(doc.getDocumentElement());

    //output as new file
    OutputStream os = new FileOutputStream("D:\\test\\new.svg");
    Writer out = new OutputStreamWriter(os, "UTF-8");

    public UsingBatikWithExistingDoc() throws IOException {
    }
   generator.stream(out,true)
   os.close();
   out.close();*/
}
