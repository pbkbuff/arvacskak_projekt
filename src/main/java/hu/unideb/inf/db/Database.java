package hu.unideb.inf.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import hu.unideb.inf.model.MealCategory;
import hu.unideb.inf.model.User;
import hu.unideb.inf.model.WeightEntry;

import java.sql.SQLException;

// sqlite + ormlite, tablakat ha nincsenek letrehozza
public class Database implements AutoCloseable {

    private final ConnectionSource connectionSource;
    private final Dao<User, Long> userDao;
    private final Dao<WeightEntry, Long> weightDao;
    private final Dao<MealCategory, Long> mealCategoryDao;

    public Database(String fileName) throws SQLException {
        connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + fileName);

        TableUtils.createTableIfNotExists(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, WeightEntry.class);
        TableUtils.createTableIfNotExists(connectionSource, MealCategory.class);

        userDao = DaoManager.createDao(connectionSource, User.class);
        weightDao = DaoManager.createDao(connectionSource, WeightEntry.class);
        mealCategoryDao = DaoManager.createDao(connectionSource, MealCategory.class);
    }

    public Dao<User, Long> getUserDao() {
        return userDao;
    }

    public Dao<WeightEntry, Long> getWeightDao() {
        return weightDao;
    }

    public Dao<MealCategory, Long> getMealCategory() {
        return mealCategoryDao;
    }

    // legutolso meres, 0 ha nincs
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
