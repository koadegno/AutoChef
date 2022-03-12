package ulb.infof307.g01.ui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import ulb.infof307.g01.cuisine.Product;
import ulb.infof307.g01.cuisine.Recipe;

import java.awt.*;
import java.util.List;

public class CreateDayColumn {
    public Callback<TableColumn<Recipe, Void>, TableCell<Recipe, Void>> createColWithButton(List<Recipe> data){
        Callback<TableColumn<Recipe, Void>, TableCell<Recipe, Void>> cellFactory = new Callback<TableColumn<Recipe, Void>, TableCell<Recipe, Void>>() {

            @Override
            public TableCell<Recipe, Void> call(TableColumn<Recipe, Void> param) {
                final TableCell<Recipe, Void> cell = new TableCell<Recipe, Void>() {

                    @Override
                    public void updateIndex(int index) {
                        super.updateIndex(index);
                        if (isEmpty() ) {//&& !data.isEmpty()
                            setText(data.get(0).getName());
                            //data.remove(0);
                        }
                    }

                };
                return cell;
            }
        };
        return cellFactory;
    }
}
