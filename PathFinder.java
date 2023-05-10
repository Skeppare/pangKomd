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
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;


public class PathFinder extends Application {

    private BorderPane root = new BorderPane();
    private VBox vBox = new VBox();
    private Pane center = new Pane();
    private Pane map = new Pane();
    private Stage stage;
    private Button newPlace;

    private ListGraph<Place> nodes = new ListGraph<>();
    private ArrayList<Place> selectedPlaces = new ArrayList<>();


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
        private Place selectedPlace1;
        private Place selectedPlace2;
        private NewConnectionAlert myAlert;

        @Override
        public void handle(ActionEvent event) {
            int selectedCount = 0;
            for (Place place : nodes.getNodes()) {
                if (place.isSelected()) {
                    selectedCount++;
                    if (selectedCount == 1) {
                        selectedPlace1 = place;
                        selectedPlaces.add(place);
                    } else if (selectedCount == 2) {
                        selectedPlace2 = place;
                        selectedPlaces.add(place);
                    }
                }
            }
            if (selectedCount < 2) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Obs");
                alert.setHeaderText("Two places must be selected!");
                alert.showAndWait();
            }
            if (selectedCount == 2) {
                if(nodes.getEdgeBetween(selectedPlace1, selectedPlace2) != null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Obs");
                    alert.setHeaderText("Connecton already exists");
                    alert.showAndWait();
                }
                myAlert = new NewConnectionAlert(selectedPlace1, selectedPlace2);
                Optional<ButtonType> result = myAlert.showAndWait();
                try {
                    if (result.isPresent() && result.get() != ButtonType.OK)
                        return;
                    String name = myAlert.getName();
                    int time = myAlert.getTime();
                    if (name.trim().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Name cant be empty!");
                        alert.showAndWait();
                        return;
                    }
                    nodes.connect(selectedPlace1, selectedPlace2, name, time);
                    Line line = new Line(selectedPlace1.getCenterX(), selectedPlace1.getCenterY(), selectedPlace2.getCenterX(), selectedPlace2.getCenterY());
                    map.getChildren().add(line);
                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Time must be positive integer!");
                    alert.showAndWait();
                    return;
                }


            }
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
                nodes.add(place);

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
            Image image = new Image("file:europa.gif");
            ImageView imageView = new ImageView(image);
            stage.setWidth(image.getWidth());
            stage.setHeight(image.getHeight() + 120);
            root.setBottom(map);
            map.getChildren().add(imageView);
        }
    }

}