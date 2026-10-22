package hu.unideb.inf;

import hu.unideb.inf.controller.MainController;
import hu.unideb.inf.db.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static final String DB_FILE = "kaloria.db";

    private Database database;

    @Override
    public void start(Stage stage) throws Exception {
        database = new Database(DB_FILE);

        FXMLLoader loader = new FXMLLoader(App.class.getResource("MainScene.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(App.class.getResource("style.css").toExternalForm());

        ((MainController) loader.getController()).setDatabase(database);

        stage.setTitle("Kalóriaszámláló");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        if (database != null) {
            database.close();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}
