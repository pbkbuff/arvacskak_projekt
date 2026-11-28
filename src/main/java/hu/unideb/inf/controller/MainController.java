package hu.unideb.inf.controller;

import hu.unideb.inf.db.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class MainController {
    private Database database;

    @FXML
    //name of this has to be formatted as <fx id>Controller
    private UserController userController;
    //@FXML
    //private FoodController foodController;
    @FXML
    private LogController logController;
    @FXML
    private Tab logTab;


    public void setDatabase(Database database) {
        this.database = database;
        userController.setDatabase(database);
        //foodController.setDatabase(database);
        logController.setDatabase(database);

        // naplo fulre valtaskor frissulnek a listak, pl. uj felhasznalo
        logTab.selectedProperty().addListener((obs, oldValue, selected) -> {
            if (selected) {
                logController.refresh();
            }
        });
    }

}
