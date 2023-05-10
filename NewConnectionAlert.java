import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

class NewConnectionAlert extends Alert {
    private TextField nameField = new TextField();
    private TextField timeField = new TextField();
    private Place name1, name2;
    public NewConnectionAlert(Place name1, Place name2) {
        super(AlertType.CONFIRMATION);
        this.name1 = name1;
        this.name2 = name2;

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(5);
        grid.setVgap(10);
        grid.addRow(0, new Label("Name:"), nameField);
        grid.addRow(1, new Label("Time:"), timeField);
        setHeaderText("Connection from " + name2 +  " to " + name1);
        getDialogPane().setContent(grid);
    }
    public String getName() {
        return nameField.getText();
    }
    public int getTime() {
        return Integer.parseInt(timeField.getText());
    }


}