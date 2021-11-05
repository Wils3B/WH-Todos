/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wh.todolist.controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Wilson
 */
public class EditTaskController implements Initializable {

    @FXML
    private Button retour;

    @FXML
    private Button valider;

    @FXML
    private TextArea libelle;

    @FXML
    private JFXTimePicker hFin;

    @FXML
    private JFXDatePicker dFin;

    @FXML
    private TextArea detail;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public Button getRetour() {
        return retour;
    }

    public Button getValider() {
        return valider;
    }

    public TextArea getLibelle() {
        return libelle;
    }

    public JFXTimePicker gethFin() {
        return hFin;
    }

    public JFXDatePicker getdFin() {
        return dFin;
    }

    public TextArea getDetail() {
        return detail;
    }

}
