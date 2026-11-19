package hu.unideb.inf.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "etel")
public class Food {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "nev", canBeNull = false)
    private String name;
    @DatabaseField(columnName = "kcal_100g", canBeNull = false)
    private  int kcal;
    @DatabaseField(columnName = "feherje_100g", canBeNull = false)
    private  int protein;
    @DatabaseField(columnName = "zsir_100g", canBeNull = false)
    private  int fat;
    @DatabaseField(columnName = "szenhidrat_100g", canBeNull = false)
    private  int carbs;

    public Food() {
    }

    public Food(int id, String name, int kcal, int protein, int fat, int carbs) {
        this.id = id;
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

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }
}
