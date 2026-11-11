package hu.unideb.inf.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "etkezes_tipus")
public class Meal {
    @DatabaseField (generatedId = true)
    private long id;

    @DatabaseField (columnName = "nev", canBeNull = false)
    private String name;

    @DatabaseField (columnName = "kcal_arany", canBeNull = false)
    private float calorieRatio;

    @DatabaseField (columnName = "sorrend", canBeNull = false)
    private int order;


    public Meal() {}

    public Meal(String name, float calorieRatio, int order) {
        this.name = name;
        this.calorieRatio = calorieRatio;
        this.order = order;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCalorieRatio() {
        return calorieRatio;
    }

    public void setCalorieRatio(float calorieRatio) {
        this.calorieRatio = calorieRatio;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}


