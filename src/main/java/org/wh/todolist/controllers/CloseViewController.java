package org.wh.todolist.controllers;

import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class CloseViewController implements Initializable {

    @FXML
    private JFXCheckBox remember;

    @FXML
    private Button stay;

    @FXML
    private Button reduct;

    @FXML
    private Button quit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public JFXCheckBox getRemember() {
        return remember;
    }

    public Button getStay() {
        return stay;
    }

    public Button getReduct() {
        return reduct;
    }

    public Button getQuit() {
        return quit;
    }
}
