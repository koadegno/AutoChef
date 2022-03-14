package ulb.infof307.g01.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.db.Database;

import java.io.IOException;
import java.sql.SQLException;

public class WindowMainMenuController {
    private Stage stage;
    private Parent root;
    private static Database dataBase = null;

    public void displayMainMenuController(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(WindowsMyShoppingListsController.class.getResource("interface/FXMLMainMenu.fxml"));
        root = loader.load();
        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene( new Scene(root));
        stage.show();
    }

    public void setDataBase(Database db){dataBase = db;}

    public void backToMainController(ActionEvent event){
        WindowMainController mainController = new WindowMainController();
        mainController.displayMain((Stage)((Node)event.getSource()).getScene().getWindow());
    }

    public void redirectToMyMenusController(ActionEvent event) throws IOException{
        WindowMyMenusController menusController = new WindowMyMenusController();
        menusController.setDatabase(dataBase);
        menusController.displayMyMenus(event);
    }
    public void redirectToCreateMenuController(ActionEvent event) throws IOException, SQLException {
        CreateMenuController createMenu = new CreateMenuController();
        createMenu.displayEditMeal(event);
    }


}
