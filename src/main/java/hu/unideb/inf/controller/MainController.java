package hu.unideb.inf.controller;

import hu.unideb.inf.App;
import hu.unideb.inf.db.Database;
import hu.unideb.inf.model.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class MainController {

    private Database database;

    @FXML private TextField nameField, heightField, weightField;
    @FXML private DatePicker birthDatePicker;
    @FXML private ComboBox<Sex> sexCombo;
    @FXML private ComboBox<ActivityLevel> activityCombo;
    @FXML private ComboBox<Goal> goalCombo;

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> colName, colAge, colHeight, colWeight, colGoal;

    @FXML
    private void initialize() {
        sexCombo.getItems().setAll(Sex.values());
        activityCombo.getItems().setAll(ActivityLevel.values());
        goalCombo.getItems().setAll(Goal.values());
        activityCombo.setValue(ActivityLevel.MERSEKELT);
        goalCombo.setValue(Goal.TARTAS);

        colName.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getName()));
        colAge.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.valueOf(c.getValue().getAge())));
        colHeight.setCellValueFactory(c -> new ReadOnlyStringWrapper(format(c.getValue().getHeightCm()) + " cm"));
        colWeight.setCellValueFactory(c -> new ReadOnlyStringWrapper(format(currentWeight(c.getValue())) + " kg"));
        colGoal.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getGoal().toString()));

        // Ha kiválasztunk egy usert, megnyitja a Napválasztót
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) handleUserLogin(newSelection);
        });
    }

    private void handleUserLogin(User selectedUser) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("MainScene1.fxml"));
            Parent root = fxmlLoader.load();

            // Adatok a következő ablaknak
            MainController1 nextController = fxmlLoader.getController();
            nextController.setDatabase(this.database);
            nextController.setCurrentUser(selectedUser);

            Stage stage = new Stage();
            stage.setTitle("Kalóriaszámláló - " + selectedUser.getName());
            stage.setScene(new Scene(root));
            stage.show();

            // Jelenlegi regisztrációs ablak bezárása
            ((Stage) userTable.getScene().getWindow()).close();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Nem sikerült megnyitni a napválasztót!");
            e.printStackTrace();
        }
    }

    public void setDatabase(Database database) {
        this.database = database;
        reloadUsers();
    }

    @FXML
    void handleSaveUser(ActionEvent event) {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) throw new IllegalArgumentException("Add meg a nevet!");

            double height = parsePositive(heightField, "Magasság");
            double weight = parsePositive(weightField, "Testsúly");
            LocalDate birthDate = birthDatePicker.getValue();

            if (birthDate == null || !birthDate.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Adj meg egy érvényes születési dátumot!");
            }
            if (sexCombo.getValue() == null || activityCombo.getValue() == null || goalCombo.getValue() == null) {
                throw new IllegalArgumentException("Válaszd ki a nemet, az aktivitást és a célt!");
            }

            User user = new User(name, height, birthDate, sexCombo.getValue(), activityCombo.getValue(), goalCombo.getValue());
            database.getUserDao().create(user);
            database.getWeightDao().create(new WeightEntry(user, LocalDate.now(), weight));

            clearForm();
            reloadUsers();
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, e.getMessage());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Mentés sikertelen: " + e.getMessage());
        }
    }

    private void reloadUsers() {
        try {
            userTable.getItems().setAll(database.getUserDao().queryForAll());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Adatbázis hiba: " + e.getMessage());
        }
    }

    private double currentWeight(User user) {
        try {
            return database.getCurrentWeight(user);
        } catch (SQLException e) {
            return 0;
        }
    }

    private void clearForm() {
        nameField.clear();
        heightField.clear();
        weightField.clear();
        birthDatePicker.setValue(null);
        sexCombo.setValue(null);
        activityCombo.setValue(ActivityLevel.MERSEKELT);
        goalCombo.setValue(Goal.TARTAS);
    }

    private static String format(double value) {
        return value == Math.rint(value) ? String.format("%.0f", value) : String.format("%.1f", value);
    }

    private static double parsePositive(TextField field, String what) {
        try {
            double value = Double.parseDouble(field.getText().trim().replace(',', '.'));
            if (value <= 0) throw new IllegalArgumentException(what + ": pozitív számot adj meg!");
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(what + ": adj meg egy számot!");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
