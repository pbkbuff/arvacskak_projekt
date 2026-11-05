package hu.unideb.inf.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "etkezesek")
public class MealEntry {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "felhasznalo_id")
    private User user;

    @DatabaseField(columnName = "nap")
    private String day;

    @DatabaseField(columnName = "napszak")
    private String mealType;

    @DatabaseField(columnName = "etel_neve")
    private String foodName;

    @DatabaseField(columnName = "kaloria")
    private double calories;

    // ÚJ MEZŐK: Szénhidrát, Zsír, Fehérje
    @DatabaseField(columnName = "szenhidrat")
    private double carbs;

    @DatabaseField(columnName = "zsir")
    private double fat;

    @DatabaseField(columnName = "feherje")
    private double protein;

    // Kötelező üres konstruktor az ORMLite-nak
    public MealEntry() {}

    public MealEntry(User user, String day, String mealType, String foodName, double calories, double carbs, double fat, double protein) {
        this.user = user;
        this.day = day;
        this.mealType = mealType;
        this.foodName = foodName;
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
    }

    // Getterek és Setterek
    public long getId() { return id; }
    public User getUser() { return user; }
    public String getDay() { return day; }
    public String getMealType() { return mealType; }
    public String getFoodName() { return foodName; }
    public double getCalories() { return calories; }
    public double getCarbs() { return carbs; }
    public double getFat() { return fat; }
    public double getProtein() { return protein; }
}
