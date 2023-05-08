import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;

public abstract class InputDialog extends Alert {

    protected GridPane grid;
    protected InputDialog(AlertType alertType) {
        super(alertType);
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(5);
        grid.setVgap(10);
        getDialogPane().setContent(grid);
    }
}
