package hu.unideb.inf.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// ki, melyik napon, melyik etkezesnel, mit es mennyit evett
@DatabaseTable(tableName = "naplo_bejegyzes")
public class DiaryEntry {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "felhasznalo_id", foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private User user;

    // yyyy-MM-dd
    @DatabaseField(columnName = "datum", canBeNull = false)
    private String date;

    @DatabaseField(columnName = "etkezes_tipus_id", foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private MealCategory mealCategory;

    @DatabaseField(columnName = "etel_id", foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Food food;

    @DatabaseField(columnName = "mennyiseg_g", canBeNull = false)
    private double amountG;

    @DatabaseField(columnName = "rogzitve", canBeNull = false)
    private String recordedAt;

    public DiaryEntry() {
    }

    public DiaryEntry(User user, LocalDate date, MealCategory mealCategory, Food food, double amountG) {
        this.user = user;
        this.date = date.toString();
        this.mealCategory = mealCategory;
        this.food = food;
        this.amountG = amountG;
        this.recordedAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString();
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getDate() {
        return LocalDate.parse(date);
    }

    public MealCategory getMealCategory() {
        return mealCategory;
    }

    public Food getFood() {
        return food;
    }

    public double getAmountG() {
        return amountG;
    }

    public LocalDateTime getRecordedAt() {
        return LocalDateTime.parse(recordedAt);
    }

    public double getKcal() {
        return food.getKcal() * amountG / 100;
    }
}
