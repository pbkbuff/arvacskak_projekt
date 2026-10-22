module hu.unideb.inf {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires ormlite.jdbc;

    opens hu.unideb.inf to javafx.fxml;
    opens hu.unideb.inf.controller to javafx.fxml;
    opens hu.unideb.inf.model to ormlite.jdbc;
    exports hu.unideb.inf;
    exports hu.unideb.inf.controller;
}
