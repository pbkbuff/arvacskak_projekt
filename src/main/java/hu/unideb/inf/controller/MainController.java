package hu.unideb.inf.controller;

import hu.unideb.inf.db.Database;
import hu.unideb.inf.model.ActivityLevel;
import hu.unideb.inf.model.Goal;
import hu.unideb.inf.model.Sex;
import hu.unideb.inf.model.User;
import hu.unideb.inf.model.WeightEntry;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;

public class MainController {

    private Database database;

    @FXML
    private TextField nameField;
    @FXML
    private TextField heightField;
    @FXML
    private TextField weightField;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private ComboBox<Sex> sexCombo;
    @FXML
    private ComboBox<ActivityLevel> activityCombo;
    @FXML
    private ComboBox<Goal> goalCombo;

    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> colName;
    @FXML
    private TableColumn<User, String> colAge;
    @FXML
    private TableColumn<User, String> colHeight;
    @FXML
    private TableColumn<User, String> colWeight;
    @FXML
    private TableColumn<User, String> colGoal;

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
    }

    public void setDatabase(Database database) {
        this.database = database;
        reloadUsers();
    }

    @FXML
    void handleSaveUser(ActionEvent event) {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Add meg a nevet!");
            }
            double height = parsePositive(heightField, "Magasság");
            double weight = parsePositive(weightField, "Testsúly");
            LocalDate birthDate = birthDatePicker.getValue();
            if (birthDate == null || !birthDate.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Adj meg egy érvényes születési dátumot!");
            }
            if (sexCombo.getValue() == null || activityCombo.getValue() == null || goalCombo.getValue() == null) {
                throw new IllegalArgumentException("Válaszd ki a nemet, az aktivitást és a célt!");
            }

            User user = new User(name, height, birthDate, sexCombo.getValue(),
                    activityCombo.getValue(), goalCombo.getValue());
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

    // segedek

    private static String format(double value) {
        return value == Math.rint(value) ? String.format("%.0f", value) : String.format("%.1f", value);
    }

    private static double parsePositive(TextField field, String what) {
        double value;
        try {
            value = Double.parseDouble(field.getText().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(what + ": adj meg egy számot!");
        }
        if (value <= 0) {
            throw new IllegalArgumentException(what + ": pozitív számot adj meg!");
        }
        return value;
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
