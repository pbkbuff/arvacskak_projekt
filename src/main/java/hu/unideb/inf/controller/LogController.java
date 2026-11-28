package hu.unideb.inf.controller;

import hu.unideb.inf.db.Database;
import hu.unideb.inf.model.DiaryEntry;
import hu.unideb.inf.model.Food;
import hu.unideb.inf.model.MealCategory;
import hu.unideb.inf.model.User;
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
import java.util.List;
import java.util.function.Predicate;

public class LogController {

    private Database database;

    @FXML
    private ComboBox<User> userCombo;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<MealCategory> mealCombo;
    @FXML
    private ComboBox<Food> foodCombo;
    @FXML
    private TextField amountField;

    @FXML
    private TableView<DiaryEntry> entryTable;
    @FXML
    private TableColumn<DiaryEntry, String> colMeal;
    @FXML
    private TableColumn<DiaryEntry, String> colFood;
    @FXML
    private TableColumn<DiaryEntry, String> colAmount;
    @FXML
    private TableColumn<DiaryEntry, String> colKcal;

    @FXML
    private void initialize() {
        colMeal.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getMealCategory().getName()));
        colFood.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getFood().getName()));
        colAmount.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                UserController.format(c.getValue().getAmountG()) + " g"));
        colKcal.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                UserController.format(c.getValue().getKcal()) + " kcal"));

        datePicker.setValue(LocalDate.now());
        userCombo.valueProperty().addListener((obs, oldValue, newValue) -> reloadEntries());
        datePicker.valueProperty().addListener((obs, oldValue, newValue) -> reloadEntries());
    }

    public void setDatabase(Database database) {
        this.database = database;
        refresh();
    }

    // listak ujratoltese, a kivalasztas megmarad
    public void refresh() {
        try {
            User user = userCombo.getValue();
            MealCategory meal = mealCombo.getValue();
            Food food = foodCombo.getValue();

            reload(userCombo, database.getUserDao().queryForAll(),
                    u -> user != null && u.getId() == user.getId());
            reload(mealCombo, database.getMealCategoryDao().queryBuilder().orderBy("sorrend", true).query(),
                    m -> meal != null && m.getId() == meal.getId());
            reload(foodCombo, database.getFoodDao().queryBuilder().orderBy("nev", true).query(),
                    f -> food != null && f.getId() == food.getId());
            reloadEntries();
        } catch (SQLException e) {
            UserController.showAlert(Alert.AlertType.ERROR, "Adatbázis hiba: " + e.getMessage());
        }
    }

    @FXML
    void handleAddEntry(ActionEvent event) {
        try {
            User user = userCombo.getValue();
            LocalDate date = datePicker.getValue();
            MealCategory meal = mealCombo.getValue();
            Food food = foodCombo.getValue();
            if (user == null) {
                throw new IllegalArgumentException("Válassz ki egy felhasználót!");
            }
            if (date == null || date.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Válassz mai vagy korábbi napot!");
            }
            if (meal == null || food == null) {
                throw new IllegalArgumentException("Válaszd ki az étkezést és az ételt!");
            }
            double amount = UserController.parsePositive(amountField, "Mennyiség");

            database.getDiaryDao().create(new DiaryEntry(user, date, meal, food, amount));

            // az etkezes marad, igy tobb etel is gyorsan felveheto
            foodCombo.getSelectionModel().clearSelection();
            foodCombo.setValue(null);
            amountField.clear();
            reloadEntries();
        } catch (IllegalArgumentException e) {
            UserController.showAlert(Alert.AlertType.WARNING, e.getMessage());
        } catch (SQLException e) {
            UserController.showAlert(Alert.AlertType.ERROR, "Mentés sikertelen: " + e.getMessage());
        }
    }

    private void reloadEntries() {
        User user = userCombo.getValue();
        LocalDate date = datePicker.getValue();
        if (database == null || user == null || date == null) {
            entryTable.getItems().clear();
            return;
        }
        try {
            entryTable.getItems().setAll(database.getDiaryEntries(user, date));
        } catch (SQLException e) {
            UserController.showAlert(Alert.AlertType.ERROR, "Adatbázis hiba: " + e.getMessage());
        }
    }

    private static <T> void reload(ComboBox<T> combo, List<T> items, Predicate<T> wasSelected) {
        T selected = items.stream().filter(wasSelected).findFirst().orElse(null);
        combo.getItems().setAll(items);
        combo.setValue(selected);
    }
}
