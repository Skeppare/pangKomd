import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

    public class Place extends Circle {

        private String name;

    public Place(String name, double x, double y) {
        super(x, y, 10);
        this.name = name;
        relocate(x, y);
        setFill(Color.BLUE);
    }

}

