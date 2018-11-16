package Main;

import Controllers.SearchForExhibitsController;
import DataModel.Exhibit;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

import static javafx.application.Application.launch;


public class SearchForExhibits  {

    private TableView table;
    public final BorderPane rootPane;

    public static void main(String[] args) {
        launch(args);
    }


    public SearchForExhibits() {

        rootPane = new BorderPane();
        ObservableList<Exhibit> data = FXCollections.observableArrayList();
        SearchForExhibitsController controller = new SearchForExhibitsController();
        ResultSet set = controller.getExhibitInfo();
        try {
            while (set.next()) {
                int numAnimals = controller.getNumAnimals(set.getString(1));
                String name = set.getString(1);
                int size = set.getInt(2);
                boolean waterFeature = set.getBoolean(3);
                data.addAll(new Exhibit(name, size, numAnimals, waterFeature));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Stage primaryStage = new Stage();
        primaryStage.setTitle("Atlanta Zoo");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Atlanta Zoo Exhibits");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Button searchButton = new Button("Search");
        HBox searchBox = new HBox(10);
        searchBox.getChildren().add(searchButton);
        grid.add(searchBox, 5, 0);

        Label name = new Label("Name:");
        grid.add(name, 0, 2);
        TextField nameTextField = new TextField();
        grid.add(nameTextField, 1, 2);

        Label animalNum = new Label("Num Animals:");
        grid.add(animalNum, 4, 2);

        Label min = new Label("Min");
        grid.add(min, 5, 1);
        final ComboBox minNumber = new ComboBox();
        minNumber.getItems().addAll(generator());
        grid.add(minNumber, 5, 2);

        Label max = new Label("Max");
        grid.add(max, 6, 1);
        final ComboBox maxNumber = new ComboBox();
        maxNumber.getItems().addAll(generator());
        grid.add(maxNumber, 6, 2);

        Label size = new Label("Size of Exhibit:");
        grid.add(size, 4, 5);

        Label minSize = new Label("Min");
        grid.add(minSize, 5, 4);
        final ComboBox minSizeNumber = new ComboBox();
        minSizeNumber.getItems().addAll(exhibitGenerator());
        grid.add(minSizeNumber, 5, 5);

        Label maxSize = new Label("Max");
        grid.add(maxSize, 6, 4);
        final ComboBox maxSizeNumber = new ComboBox();
        maxSizeNumber.getItems().addAll(exhibitGenerator());
        grid.add(maxSizeNumber, 6, 5);

        Label waterFeature = new Label("Water Feature: ");
        grid.add(waterFeature, 0, 5);
        final ComboBox waterFeatureBox = new ComboBox();
        waterFeatureBox.getItems().addAll(
                "Yes", "No"
        );
        grid.add(waterFeatureBox, 1, 5);

        table = new TableView<>();

        table.setRowFactory(tv -> {
            TableRow<Exhibit> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Exhibit rowData = row.getItem();
                    ExhibitDetail exhibitDetail = new ExhibitDetail(rowData.name, rowData.size, rowData.numAnimals, rowData.waterFeature);
                    primaryStage.getScene().setRoot(exhibitDetail.getRootPane());
                    primaryStage.hide();
                }
            });
            return row ;
        });

        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<Exhibit, String>("name"));
        TableColumn sizeCol = new TableColumn("Size");
        sizeCol.setCellValueFactory(new PropertyValueFactory<Exhibit, String>("size"));
        TableColumn animalNumCol = new TableColumn("Number of Animals");
        animalNumCol.setCellValueFactory(new PropertyValueFactory<Exhibit, String>("numAnimals"));
        TableColumn waterCol = new TableColumn("Water");
        waterCol.setCellValueFactory(new PropertyValueFactory<Exhibit, String>("waterFeature"));


        table.getColumns().setAll(nameCol, sizeCol, animalNumCol, waterCol);
        table.setItems(data);
        table.setPrefWidth(400);
        table.setPrefHeight(200);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        grid.add(table, 0, 7, 7, 7);

        Group root = new Group();
        root.getChildren().addAll(grid);

        Scene scene = new Scene(root,580, 500);
        primaryStage.setScene(scene);

        primaryStage.show();

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = nameTextField.getText();
                int minNum = (int) minNumber.getValue();
                int maxNum = (int) maxNumber.getValue();
                int minSize = (int) minSizeNumber.getValue();
                int maxSize = (int) maxSizeNumber.getValue();
                String water = (String) waterFeatureBox.getValue();
                SearchForExhibitsController controller = new SearchForExhibitsController();
                ResultSet set = controller.searchButtonPressed(name, minNum, maxNum, water, minSize, maxSize);
                ObservableList<Exhibit> data = FXCollections.observableArrayList();
                try {
                    while (set.next()) {
                        int numAnimals = controller.getNumAnimals(set.getString(1));
                        data.addAll(new Exhibit(set.getString(1), set.getInt(2), numAnimals, set.getBoolean(3)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                table.getItems().clear();
                table.getItems().addAll(data);

            }
        });

    }

    private Integer[] generator() {
        int size = 100;
        Integer[] result = new Integer[size];

        for (int i = 0; i < result.length; i++) {
            result[i] = i;

        }
        return result;
    }

    private Integer[] exhibitGenerator() {
        int size = 50;
        Integer[] result = new Integer[size];
        result[0] = 0;

        for (int i = 1; i < result.length; i++) {
            result[i] = result[i-1] + 50;

        }
        return result;
    }

    public Pane getRootPane() {
        return rootPane;
    }
}
