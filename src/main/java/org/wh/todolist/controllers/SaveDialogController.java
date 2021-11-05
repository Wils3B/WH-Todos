package org.wh.todolist.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SaveDialogController implements Initializable {

    @FXML
    private Button retour;

    @FXML
    private Button valider;

    @FXML
    private TextField titre;

    @FXML
    private TextField cheminField;

    @FXML
    private Button cheminBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Button getRetour() {
        return retour;
    }

    public Button getValider() {
        return valider;
    }

    public TextField getTitre() {
        return titre;
    }

    public TextField getCheminField() {
        return cheminField;
    }

    public Button getCheminBtn() {
        return cheminBtn;
    }
}
