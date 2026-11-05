package hu.unideb.inf.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import hu.unideb.inf.model.User;
import hu.unideb.inf.model.WeightEntry;
import hu.unideb.inf.model.MealEntry; // <-- ÚJ IMPORT

import java.sql.SQLException;

public class Database implements AutoCloseable {

    private final ConnectionSource connectionSource;
    private final Dao<User, Long> userDao;
    private final Dao<WeightEntry, Long> weightDao;
    private final Dao<MealEntry, Long> mealDao;

    public Database(String fileName) throws SQLException {
        connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + fileName);

        TableUtils.createTableIfNotExists(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, WeightEntry.class);
        TableUtils.createTableIfNotExists(connectionSource, MealEntry.class);

        userDao = DaoManager.createDao(connectionSource, User.class);
        weightDao = DaoManager.createDao(connectionSource, WeightEntry.class);
        mealDao = DaoManager.createDao(connectionSource, MealEntry.class);
    }

    public Dao<User, Long> getUserDao() {
        return userDao;
    }

    public Dao<WeightEntry, Long> getWeightDao() {
        return weightDao;
    }

    // ÚJ GETTER, hogy elérjük a naplózót a kontrollerből
    public Dao<MealEntry, Long> getMealDao() {
        return mealDao;
    }

    public double getCurrentWeight(User user) throws SQLException {
        WeightEntry last = weightDao.queryBuilder()
                .orderBy("datum", false)
                .where().eq("felhasznalo_id", user.getId())
                .queryForFirst();
        return last == null ? 0 : last.getWeightKg();
    }

    @Override
    public void close() throws Exception {
        connectionSource.close();
    }
}
