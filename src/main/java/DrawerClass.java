import graphics.Canvas;
import graphics.Color;
import graphics.Ellipse;
import graphics.Rectangle;

public class DrawerClass {
    public static void main(String[] args) {
        Rectangle box = new Rectangle(5, 10, 60, 80);
        /*box.draw();
        Ellipse egg = new Ellipse(100, 100, 40, 60);
        egg.setColor(Color.YELLOW);
        egg.fill();*/

        Canvas canvas = Canvas.getInstance();

        canvas.show(box);

        canvas.saveToDisk("box.png");
    }
}
