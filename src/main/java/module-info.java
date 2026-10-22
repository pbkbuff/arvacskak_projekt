module hu.unideb.inf {
    requires javafx.controls;
    requires javafx.fxml;

    opens hu.unideb.inf to javafx.fxml;
    exports hu.unideb.inf;
    exports hu.unideb.inf.controller;
    opens hu.unideb.inf.controller to javafx.fxml;
}