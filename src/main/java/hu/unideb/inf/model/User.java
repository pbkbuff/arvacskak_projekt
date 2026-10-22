package hu.unideb.inf.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;
import java.time.Period;

@DatabaseTable(tableName = "felhasznalo")
public class User {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "nev", canBeNull = false)
    private String name;

    @DatabaseField(columnName = "magassag_cm", canBeNull = false)
    private double heightCm;

    // yyyy-MM-dd, ormlite nem tud LocalDate-et
    @DatabaseField(columnName = "szuletesi_datum", canBeNull = false)
    private String birthDate;

    @DatabaseField(columnName = "nem", canBeNull = false)
    private Sex sex;

    @DatabaseField(columnName = "aktivitas", canBeNull = false)
    private ActivityLevel activity;

    @DatabaseField(columnName = "cel", canBeNull = false)
    private Goal goal;

    // ormlite nak kell
    public User() {
    }

    public User(String name, double heightCm, LocalDate birthDate, Sex sex, ActivityLevel activity, Goal goal) {
        this.name = name;
        this.heightCm = heightCm;
        this.birthDate = birthDate.toString();
        this.sex = sex;
        this.activity = activity;
        this.goal = goal;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getHeightCm() {
        return heightCm;
    }

    public LocalDate getBirthDate() {
        return LocalDate.parse(birthDate);
    }

    public int getAge() {
        return Period.between(getBirthDate(), LocalDate.now()).getYears();
    }

    public Sex getSex() {
        return sex;
    }

    public ActivityLevel getActivity() {
        return activity;
    }

    public Goal getGoal() {
        return goal;
    }

    @Override
    public String toString() {
        return name;
    }
}
