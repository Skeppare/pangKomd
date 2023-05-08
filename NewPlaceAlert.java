import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NewPlaceAlert extends InputDialog {

    private TextField placeField = new TextField();
    public NewPlaceAlert() {
        super(AlertType.CONFIRMATION);
        grid.addRow(0, new Label("Name of place:"), placeField);
    }
    public String getName() {
        return placeField.getText();
    }


    public String spawnNewPlaceDialog() {
        setTitle("Name");
        Optional<ButtonType> result = showAndWait();
        if (result.get() == ButtonType.OK) {
            return getName();
        }
        return "";
    }

}
