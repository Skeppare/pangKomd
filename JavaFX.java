import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;


public class JavaFX extends Application {

    private BorderPane root = new BorderPane();
    private VBox vBox = new VBox();
    // private FlowPane center = new FlowPane();
    private Pane center = new Pane();
    private Pane map = new Pane();
    private Stage stage;
    private Button newPlace;

    private ListGraph<Place> list = new ListGraph<>();

    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        Menu fileMenu = new Menu("File");

        MenuItem newMapMenuItem = new MenuItem("New Map");
        newMapMenuItem.setOnAction(new newMapHandler());

        MenuItem openMenuItem = new MenuItem("Open");
        openMenuItem.setOnAction((new OpenItemHandler()));
        MenuItem saveMenuItem = new MenuItem("Save");
        saveMenuItem.setOnAction(new SaveMenuHandler());
        MenuItem saveImageMenuItem = new MenuItem("Save Image");
        saveImageMenuItem.setOnAction(new SaveImageMenuHandler());
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(new ExitMenuHandler());

        fileMenu.getItems().addAll(newMapMenuItem, openMenuItem, saveMenuItem, saveImageMenuItem, new SeparatorMenuItem(), exitMenuItem);

        MenuBar menuBar = new MenuBar(fileMenu);
        vBox.getChildren().add(menuBar);
        root.setTop(vBox);


        /////////////////////////////////////////////////////////////////////////

        FlowPane controls = new FlowPane();
        root.setCenter(controls);
        controls.setAlignment(Pos.TOP_CENTER);
        controls.setPadding(new Insets(5));
        controls.setHgap(5);
        Button findPath = new Button("Find Path");
        findPath.setOnAction(new FindHandler());
        Button showConnection = new Button("Show Connection");
        showConnection.setOnAction(new ShowConnectionHandler());


        newPlace = new Button("New Place");
        newPlace.setOnAction(new NewPlaceHandler());


        Button newConnection = new Button("New Connection");
        newConnection.setOnAction(new NewConnectionHandler());
        Button changeConnection = new Button("Change Connection");
        changeConnection.setOnAction(new ChangeConnectionHandler());
        controls.getChildren().addAll(findPath, showConnection, newPlace, newConnection, changeConnection);

        Scene scene = new Scene(root, 540, 80);


        primaryStage.setScene(scene);
        primaryStage.setTitle("Path Finder");
        primaryStage.show();

    }

    class ChangeConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
        }
    }
    class NewConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {

        }
    }

    class ConnectionWindow extends InputDialog {
        private TextField connectionName = new TextField();
        private TextField time = new TextField();

        public ConnectionWindow() {
            super(AlertType.CONFIRMATION);

            grid.addRow(0, new Label("Name:"), connectionName);
            grid.addRow(1, new Label("Time:"), time);
        }

    }


    class NewPlaceHandler implements EventHandler<ActionEvent> {
        private boolean isUsed = false;

        @Override
        public void handle(ActionEvent event) {

            if (!isUsed) {
                map.setOnMouseClicked(new KlickHandler(this));
                newPlace.setDisable(true);
                map.setCursor(Cursor.CROSSHAIR);
                isUsed = true;
            }
        }
        public void reset() {
            isUsed = false;
            newPlace.setDisable(false);
            map.setCursor(Cursor.DEFAULT);
        }
    }

    class KlickHandler implements EventHandler<MouseEvent> {

        private final NewPlaceHandler newPlaceHandler;
        private boolean isUsed = false;

        public KlickHandler(NewPlaceHandler newPlaceHandler) {
            this.newPlaceHandler = newPlaceHandler;
        }

        @Override
        public void handle(MouseEvent event) {

            if (!isUsed) {
                Place place = new Place(event.getX(), event.getY());
                map.getChildren().add(place);

                NewPlaceAlert dialog = new NewPlaceAlert();
                String name = dialog.spawnNewPlaceDialog();
                place.setName(name);
                list.add(place);

                Label label = new Label(name);
                label.setLayoutX(place.getCenterX() - label.getWidth() / 2);
                label.setLayoutY(place.getCenterY() - label.getHeight() / 2);
                map.getChildren().add(label);
                isUsed = true;
                newPlaceHandler.reset();
            }
        }
    }

    class ShowConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
        }
    }

    class FindHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    class ExitMenuHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Obs");
            alert.setHeaderText("Avsluta utan att spara?");
            Optional<ButtonType> svar = alert.showAndWait();
            if (svar.isPresent() && svar.get() == ButtonType.CANCEL) {
                event.consume();
            } else if (svar.isPresent() && svar.get() == ButtonType.OK) {
                stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            }
        }
    }

    class SaveImageMenuHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try {
                WritableImage image = root.snapshot(null, null);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ImageIO.write(bufferedImage, "png", new File("capture.png"));
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Fel");
                alert.showAndWait();
            }
        }
    }

    class SaveMenuHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
        }
    }

    class OpenItemHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
        }
    }
    class newMapHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Image image = new Image("file:C:/europa.gif");
            ImageView imageView = new ImageView(image);
            stage.setWidth(image.getWidth());
            stage.setHeight(image.getHeight() + 120);
            root.setBottom(map);
            map.getChildren().add(imageView);
        }
    }

}