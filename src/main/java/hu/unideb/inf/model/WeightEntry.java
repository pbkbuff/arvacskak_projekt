package hu.unideb.inf.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;

// suly tortenet aktualis = legutolso
@DatabaseTable(tableName = "suly_meres")
public class WeightEntry {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "felhasznalo_id", foreign = true, canBeNull = false, uniqueCombo = true)
    private User user;

    // yyyy-MM-dd
    @DatabaseField(columnName = "datum", canBeNull = false, uniqueCombo = true)
    private String date;

    @DatabaseField(columnName = "suly_kg", canBeNull = false)
    private double weightKg;

    public WeightEntry() {
    }

    public WeightEntry(User user, LocalDate date, double weightKg) {
        this.user = user;
        this.date = date.toString();
        this.weightKg = weightKg;
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

    public double getWeightKg() {
        return weightKg;
    }
}
