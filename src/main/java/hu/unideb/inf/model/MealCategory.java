package hu.unideb.inf.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "etkezes_tipus")
public class MealCategory {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "nev", canBeNull = false)
    private String name;

    @DatabaseField(columnName = "kcal_arany", canBeNull = false)
    private float kcal_arany;

    @DatabaseField(columnName = "sorrend", canBeNull = false)
    private int sorrend;

    public MealCategory(){
    }

    public MealCategory(String name, float kcal_arany, int sorrend) {
        this.name = name;
        this.kcal_arany = kcal_arany;
        this.sorrend = sorrend;
    }

    public long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public float getKcal_arany(){
        return kcal_arany;
    }

    public int getSorrend(){
        return sorrend;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setKcal_arany(float kcal_arany){
        this.kcal_arany = kcal_arany;
    }

    public void setSorrend(int sorrend){
        this.sorrend = sorrend;
    }
}
