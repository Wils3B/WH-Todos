package org.wh.todolist.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuViewController implements Initializable {

    @FXML
    private Button openBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button settingsBtn;

    @FXML
    private Button aboutBtn;

    @FXML
    private Button quitBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Button getOpenBtn() {
        return openBtn;
    }

    public Button getSaveBtn() {
        return saveBtn;
    }

    public Button getSettingsBtn() {
        return settingsBtn;
    }

    public Button getAboutBtn() {
        return aboutBtn;
    }

    public Button getQuitBtn() {
        return quitBtn;
    }
}
