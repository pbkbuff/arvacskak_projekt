package hu.unideb.inf;

import hu.unideb.inf.controller.FXMLStudentsSceneController;
import hu.unideb.inf.model.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("FXMLStudentsScene.fxml"));
        Scene scene = new Scene(loader.load());

        ((FXMLStudentsSceneController)loader.getController()).setModel(new Model());

        stage.setTitle("Students Register");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}