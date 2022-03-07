package ulb.infof307.g01.ui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.util.Callback;
import ulb.infof307.g01.cuisine.Product;
import ulb.infof307.g01.cuisine.Recipe;
import ulb.infof307.g01.cuisine.TempMenu;


public class WindowShowMenuController implements Initializable {

    private TempMenu menu;

    public void setMenu(String name, LocalDate[] duration, int numOfDays){
        this.menu= new TempMenu(name, duration, numOfDays);
        displayMenuInfo(name);
    }

    @FXML
    DatePicker dateBegin, dateEnd;

    private Stage stage;
    private Scene scene;
    //private Parent root;

    @FXML
    TableView menuTable;

    @FXML
    Label menuName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // displayMenuInfo(menu.name);
        this.menuName.setText(" ");
        //menuTable.setEditable(false);
    }

    @FXML
    public void displayMenuInfo(String name){
        this.menuName.setText(name);
        dateBegin.setValue(menu.duration[0]);
        dateEnd.setValue(menu.duration[1]);
        displayMenuTable(menu);
    }

    @FXML
    public void displayMenuTable(TempMenu menu){
        System.out.println("table");
        int numOfDays= this.menu.numOfDays;
        ObservableList<TableColumn> cols = FXCollections.observableArrayList();
        TableColumn columnDelete = new TableColumn("test column");
        ObservableList<Recipe> data = FXCollections.observableArrayList(
                new Recipe("recette 1"),
                new Recipe("recette 2"),
                new Recipe("recette 3"),
                new Recipe("recette 4"),
                new Recipe("recette 5"),
                new Recipe("recette 6"),
                new Recipe("recette 7"),
                new Recipe("recette 8")
                );
        //menuTable.get
        //menuTable.setItems(FXCollections.observableArrayList("recette_test"));
        //TODO: CORRIGER!!
        menuTable.getItems().add(null);
        CreateDayColumn createDayColumn = new CreateDayColumn();
        Callback<TableColumn<Recipe, Void>, TableCell<Recipe, Void>> cellFactory = createDayColumn.createColWithButton(data);
        columnDelete.setCellFactory(cellFactory);


        /*for (int i = 0; i < numOfDays; i++) {
            int dayIndex = i-1;
            TableColumn<Recipe, String> column = new TableColumn<>("Day " + i);
            /*column.setCellValueFactory(cd -> {
                Recipe recipe = cd.getValue()[dayIndex];
                return new SimpleObjectProperty<>(recipe == null ? null : recipe.getName());
            });
            //column.getCellData(i);
            column.setCellValueFactory(new PropertyValueFactory<>("name"));
            //column.
            //cols.add(column);
        }
        menuTable.getColumns().addAll(cols);
        ObservableList<Recipe> listeRecipes = FXCollections.observableArrayList();
        menuTable.setItems(listeRecipes);*/

        /*
        TableColumn day0 = new TableColumn<>("Name");
        day0.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn day1 = new TableColumn("Qté");
        day1.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        //String[] mardi =  new String[3];
        //mardi[0]="Recette0";
        //mardi[1]="Recette1";
        //mardi[2]="Recette2";

        //day0.setText("Lundi");
        //for (int i = 0; i < mardi.length; i++) {
           // menuTable.getItems().add(mardi[i]);
        //}
        //day1.setText("Mardi");
        //day2.setText("Mercredi");

        menuTable.getColumns().addAll(day0, day1);

        menuTable.getItems().add(new Product("Tomate", 100));
        menuTable.getItems().add(new Product("Pommes", 50));

 */

        menuTable.getColumns().add(columnDelete);
    }

    @FXML
    public void displayMenu(MouseEvent mousePressed, String name){
        try {
            System.out.println("Menu existe");

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("interface/FXMLShowMenu.fxml")));  //          Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("interface/FXMLShowMenu.fxml")));
            /*LocalDate dateBegin = LocalDate.of(2022, 3, 10);
            LocalDate dateEnd = LocalDate.of(2017, 3, 17);
            LocalDate[] duration =  {dateBegin, dateEnd};
            this.menu = new TempMenu(name, duration, 3);

             */
            //displayMenuInfo(name);

            stage = (Stage) ((Node)mousePressed.getSource()).getScene().getWindow();
            scene =  new Scene(root);
            stage.setTitle("Menu "+name);
            stage.setScene(scene);
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void back(ActionEvent event) throws IOException {
        WindowMyMenusController menu = new WindowMyMenusController();
        menu.displayMenuList(event);
    }
}
