package hu.unideb.inf.controller;

import hu.unideb.inf.App;
import hu.unideb.inf.db.Database;
import hu.unideb.inf.model.User;
import hu.unideb.inf.model.MealEntry;
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
import java.util.List;

public class LoggingController {

    private Database database;
    private User currentUser;
    private String currentDay;
    private String currentMeal;

    @FXML private Label infoLabel, totalCaloriesLabel;
    @FXML private TextField foodNameField, caloriesField, carbsField, fatField, proteinField; // ÚJ MEZŐK REGISZTRÁLVA

    // Tábla cuccai új oszlopokkal
    @FXML private TableView<MealEntry> mealTable;
    @FXML private TableColumn<MealEntry, String> colFoodName, colCalories, colCarbs, colFat, colProtein;

    @FXML
    private void initialize() {
        // Oszlopok összekötése a MealEntry-vel (az újak is)
        colFoodName.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getFoodName()));
        colCalories.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.format("%.0f kcal", c.getValue().getCalories())));
        colCarbs.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.format("%.1f g", c.getValue().getCarbs())));
        colFat.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.format("%.1f g", c.getValue().getFat())));
        colProtein.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.format("%.1f g", c.getValue().getProtein())));
    }

    public void setDatabase(Database database) { this.database = database; }
    public void setCurrentUser(User user) { this.currentUser = user; }

    public void setContext(String day, String meal) {
        this.currentDay = day;
        this.currentMeal = meal;
        if (infoLabel != null) {
            infoLabel.setText("Naplózás: " + day + " - " + meal);
        }
        // Miután minden adat (user, day, meal) megvan, ráfrissítünk
        refreshTable();
    }

    // Adatbázis szűrés és táblázat frissítés makró összegzéssel
    private void refreshTable() {
        if (database == null || currentUser == null || currentDay == null || currentMeal == null) return;

        try {
            // ORMLite QueryBuilder a pontos szűrés miatt
            List<MealEntry> filteredMeals = database.getMealDao().queryBuilder().where()
                    .eq("felhasznalo_id", currentUser.getId()).and()
                    .eq("nap", currentDay).and()
                    .eq("napszak", currentMeal).query();

            // Adatok listázása
            mealTable.getItems().setAll(filteredMeals);

            // Összesített kcal és makrók számolása
            double totalKcal = filteredMeals.stream().mapToDouble(MealEntry::getCalories).sum();
            double totalCarbs = filteredMeals.stream().mapToDouble(MealEntry::getCarbs).sum();
            double totalFat = filteredMeals.stream().mapToDouble(MealEntry::getFat).sum();
            double totalProtein = filteredMeals.stream().mapToDouble(MealEntry::getProtein).sum();

            totalCaloriesLabel.setText(String.format("Összesen: %.0f kcal | CH: %.1fg | Zsír: %.1fg | Fehérje: %.1fg",
                    totalKcal, totalCarbs, totalFat, totalProtein));

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Nem sikerült betölteni az ételeket: " + e.getMessage());
        }
    }

    // Valamennyire hülyebiztossá alakított mentés az új makrókkal
    @FXML
    private void handleSaveLog(ActionEvent event) {
        try {
            String name = foodNameField.getText().trim();
            if (name.isEmpty()) throw new IllegalArgumentException("Az étel nevét kötelező kitölteni!");

            // Számok beolvasása és validálása a segédmetódussal
            double calories = parsePositive(caloriesField, "Kalória");
            double carbs = parsePositive(carbsField, "Szénhidrát");
            double fat = parsePositive(fatField, "Zsír");
            double protein = parsePositive(proteinField, "Fehérje");

            // Mentés az adatbázisba az új adatszerkezettel
            database.getMealDao().create(new MealEntry(currentUser, currentDay, currentMeal, name, calories, carbs, fat, protein));

            // Mezők törlése
            foodNameField.clear();
            caloriesField.clear();
            carbsField.clear();
            fatField.clear();
            proteinField.clear();

            // táblázat frissítése hogy egyből bekerüljön a listába
            refreshTable();

        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, e.getMessage());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Adatbázis hiba történt: " + e.getMessage());
        }
    }

    // Segédmetódus a pozitív számok beolvasására
    private static double parsePositive(TextField field, String what) {
        try {
            double value = Double.parseDouble(field.getText().trim().replace(',', '.'));
            if (value < 0) throw new IllegalArgumentException(what + ": nem lehet negatív szám!");
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(what + ": adj meg egy érvényes számot!");
        }
    }

    @FXML
    private void handleGoBack(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("MainScene1.fxml"));
            Parent root = fxmlLoader.load();

            MainController1 selectionController = fxmlLoader.getController();
            selectionController.setDatabase(this.database);
            selectionController.setCurrentUser(this.currentUser);
            selectionController.setSelection(currentDay, currentMeal);

            Stage stage = new Stage();
            stage.setTitle("Kalóriaszámláló - " + currentUser.getName());
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
