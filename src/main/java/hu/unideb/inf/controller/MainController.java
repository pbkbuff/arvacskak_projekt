package hu.unideb.inf.controller;

import hu.unideb.inf.db.Database;
import javafx.fxml.FXML;

public class MainController {
    private Database database;

    @FXML
    //name of this has to be formatted as <fx id>Controller
    private UserController userController;
    //@FXML
    //private FoodController foodController;


    public void setDatabase(Database database) {
        this.database = database;
        userController.setDatabase(database);
        //foodController.setDatabase(database);
    }

}
