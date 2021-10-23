package org.wh.todolist.controllers;

import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.wh.materials.core.Settings;
import org.wh.todolist.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsViewController implements Initializable {

    @FXML
    private Button retour;

    @FXML
    private Button valider;

    @FXML
    private JFXCheckBox restorePrev;

    @FXML
    private JFXCheckBox closedDialog;

    @FXML
    private JFXCheckBox openEdit;

    @FXML
    private JFXCheckBox openAppend;

    @FXML
    private JFXCheckBox alwaysOnTop;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateFieldsFromSettings();
    }

    public void updateFieldsFromSettings() {
        Settings settings = Main.getSettings();
        restorePrev.setSelected((Boolean) settings.getSetting("restoreSession"));
        closedDialog.setSelected((Boolean) settings.getSetting("showClosedDialog"));
        openEdit.setSelected((Boolean) settings.getSetting("editTaskOnCreate"));
        openAppend.setSelected(!(Boolean) settings.getSetting("openAppend"));
        alwaysOnTop.setSelected((Boolean) settings.getSetting("alwaysOnTop"));
    }

    public void updateSettingsFromFields() {
        Settings settings = Main.getSettings();
        settings.insertSetting("restoreSession", restorePrev.isSelected());
        settings.insertSetting("showClosedDialog", closedDialog.isSelected());
        settings.insertSetting("editTaskOnCreate", openEdit.isSelected());
        settings.insertSetting("openAppend", !openAppend.isSelected());
        settings.insertSetting("alwaysOnTop", alwaysOnTop.isSelected());
    }

    public Button getRetour() {
        return retour;
    }

    public Button getValider() {
        return valider;
    }
}
