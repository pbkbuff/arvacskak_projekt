package hu.unideb.inf.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import hu.unideb.inf.model.Food;
import hu.unideb.inf.model.MealCategory;
import hu.unideb.inf.model.User;
import hu.unideb.inf.model.WeightEntry;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

// sqlite + ormlite, tablakat ha nincsenek letrehozza
public class Database implements AutoCloseable {

    private final ConnectionSource connectionSource;
    private final Dao<User, Long> userDao;
    private final Dao<WeightEntry, Long> weightDao;
    private final Dao<Food, Integer> foodDao;
    private final Dao<MealCategory, Long> mealCategoryDao;

    public Database(String fileName) throws SQLException {
        connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + fileName);

        TableUtils.createTableIfNotExists(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, WeightEntry.class);
        TableUtils.createTableIfNotExists(connectionSource, Food.class);
        TableUtils.createTableIfNotExists(connectionSource, MealCategory.class);

        userDao = DaoManager.createDao(connectionSource, User.class);
        weightDao = DaoManager.createDao(connectionSource, WeightEntry.class);
        foodDao = DaoManager.createDao(connectionSource, Food.class);
        mealCategoryDao = DaoManager.createDao(connectionSource, MealCategory.class);

        seedMealCategories();
        seedFoods();
    }

    public Dao<User, Long> getUserDao() {
        return userDao;
    }

    public Dao<WeightEntry, Long> getWeightDao() {
        return weightDao;
    }

    public Dao<Food, Integer> getFoodDao() { return foodDao; }

    public Dao<MealCategory, Long> getMealCategoryDao() {
        return mealCategoryDao;
    }

    // alap etkezesek, csak ha ures a tabla. az aranyok osszege 1.0
    private void seedMealCategories() throws SQLException {
        if (mealCategoryDao.countOf() > 0) {
            return;
        }
        mealCategoryDao.create(List.of(
                new MealCategory("Reggeli", 0.25f, 1),
                new MealCategory("Ebéd", 0.35f, 2),
                new MealCategory("Vacsora", 0.30f, 3),
                new MealCategory("Nasi", 0.10f, 4)));
    }

    // alap etelek, kozelito ertekek 100 g-ra: kcal, feherje, zsir, szenhidrat
    // forras: USDA adatok (masodlagos oldalakrol), tej es sajt: magyar bolti termekadatok
    private void seedFoods() throws SQLException {
        if (foodDao.countOf() > 0) {
            return;
        }
        foodDao.create(List.of(
                new Food("Fehér kenyér", 270, 9.4, 3.3, 49.2),
                new Food("Tojás", 143, 12.6, 9.5, 0.7),
                new Food("Csirkemell (nyers)", 120, 22.5, 2.6, 0),
                new Food("Rizs (főtt)", 130, 2.7, 0.3, 28),
                new Food("Tészta (főtt)", 158, 5.8, 0.9, 31),
                new Food("Burgonya (főtt)", 86, 1.7, 0.1, 20),
                new Food("Tej (2,8%)", 56, 3.0, 2.8, 4.7),
                new Food("Trappista sajt", 343, 25.2, 25.7, 2.5),
                new Food("Alma", 52, 0.3, 0.2, 14),
                new Food("Banán", 89, 1.1, 0.3, 23)));
    }

    // legutolso meres, 0 ha nincs
    public double getCurrentWeight(User user) throws SQLException {
        WeightEntry last = weightDao.queryBuilder()
                .orderBy("datum", false)
                .where().eq("felhasznalo_id", user.getId())
                .queryForFirst();
        return last == null ? 0 : last.getWeightKg();
    }

    // naponta egy meres, false ha ma mar van
    public boolean addTodayWeight(User user, double weightKg) throws SQLException {
        LocalDate today = LocalDate.now();
        long count = weightDao.queryBuilder()
                .where().eq("felhasznalo_id", user.getId())
                .and().eq("datum", today.toString())
                .countOf();
        if (count > 0) {
            return false;
        }
        weightDao.create(new WeightEntry(user, today, weightKg));
        return true;
    }

    @Override
    public void close() throws Exception {
        connectionSource.close();
    }
}
