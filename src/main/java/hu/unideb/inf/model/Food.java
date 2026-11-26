package hu.unideb.inf.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "etel")
public class Food {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "nev", canBeNull = false, unique = true)
    private String name;
    @DatabaseField(columnName = "kcal_100g", canBeNull = false)
    private double kcal;
    @DatabaseField(columnName = "feherje_100g", canBeNull = false)
    private double protein;
    @DatabaseField(columnName = "zsir_100g", canBeNull = false)
    private double fat;
    @DatabaseField(columnName = "szenhidrat_100g", canBeNull = false)
    private double carbs;

    public Food() {
    }

    public Food(String name, double kcal, double protein, double fat, double carbs) {
        this.name = name;
        this.kcal = kcal;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }
}
