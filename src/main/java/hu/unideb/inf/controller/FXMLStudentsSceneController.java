package hu.unideb.inf.controller;

import hu.unideb.inf.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FXMLStudentsSceneController {

    private Model model;

    public void setModel(Model model) {
        this.model = model;
    }

    @FXML
    private Label seasonsLabel;

    @FXML
    private Label creditsLabel;

    @FXML
    private Label dateOfBirthLabel;

    @FXML
    private Label nameLabel;

    @FXML
    void handleLoadButtonPressed(ActionEvent event) {
        refreshNameLabel();
        creditsLabel.setText("" + model.getStudent().getCredits());
        dateOfBirthLabel.setText(model.getStudent().getDateOfBirth().toString());
        System.out.println("F I R E W O R K ! ! ! \uD83C\uDF86");
    }

    @FXML
    void handleChangeButtonPressed(ActionEvent event) {
        model.getStudent().setName("John Smith");
        refreshNameLabel();
    }

    private void refreshNameLabel() {
        nameLabel.setText(model.getStudent().getName());
    }

    @FXML
    void handleButtonPressed(ActionEvent event) {
        if (seasonsLabel.getText().equals("Winter"))
            seasonsLabel.setText("Summer");
        else
            seasonsLabel.setText("Winter");
    }

}
