package hu.unideb.inf.controller;

import hu.unideb.inf.App;
import hu.unideb.inf.db.Database;
import hu.unideb.inf.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController1 {

    private Database database;
    private User currentUser;

    @FXML private Label welcomeLabel;
    @FXML private ComboBox<String> dayComboBox;
    @FXML private ComboBox<String> mealComboBox;

    public void setDatabase(Database database) { this.database = database; }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (welcomeLabel != null && user != null) {
            welcomeLabel.setText("Felhasználó: " + user.getName());
        }
    }

    // Előre beállítja az értékeket ha a naplózásból jövünk VISSZA
    public void setSelection(String day, String meal) {
        dayComboBox.setValue(day);
        mealComboBox.setValue(meal);
    }

    @FXML
    private void handleOpenLoggingScene(ActionEvent event) {
        String selectedDay = dayComboBox.getValue();
        String selectedMeal = mealComboBox.getValue();

        if (selectedDay == null || selectedMeal == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Kérlek válassz ki egy napot és egy étkezést is!");
            alert.showAndWait();
            return;
        }

        try {
            // Betöltjük az ÚJ LoggingScene.fxml-t
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("LoggingScene.fxml"));
            Parent root = fxmlLoader.load();

            // Adatokat átadás az új logger ablaknak
            LoggingController loggingController = fxmlLoader.getController();
            loggingController.setDatabase(this.database);
            loggingController.setCurrentUser(this.currentUser);
            loggingController.setContext(selectedDay, selectedMeal);

            Stage stage = new Stage();
            stage.setTitle("Naplózás - " + selectedDay + " (" + selectedMeal + ")");
            stage.setScene(new Scene(root));
            stage.show();

            // Bezárjuk a választó ablakot
            ((Stage) dayComboBox.getScene().getWindow()).close();

        } catch (IOException e) {
            System.err.println("Nem sikerült betölteni a LoggingScene.fxml fájlt!");
            e.printStackTrace();
        }
    }
}
