/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wh.todolist.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Wilson
 */
public class CentralViewController implements Initializable {

    @FXML
    private Button markAll;

    @FXML
    private TextField addTask;

    @FXML
    private VBox taskList;

    @FXML
    private Label taskNumber;

    @FXML
    private ToggleButton allBtn;

    private ToggleGroup group;

    @FXML
    private ToggleButton finishedBtn;

    @FXML
    private ToggleButton unfinishedBtn;

    @FXML
    private Button vider;

    @FXML
    private Button openMenu;

    @FXML
    private ScrollPane centralScroll;

    /**
     * Initializes the controller class.
     *
     * @param url l'url
     * @param rb  la ressource
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        group = new ToggleGroup();
        group.getToggles().addAll(allBtn, finishedBtn, unfinishedBtn);
    }

    public Button getMarkAll() {
        return markAll;
    }

    public TextField getAddTask() {
        return addTask;
    }

    public ToggleGroup getGroup() {
        return group;
    }

    public Button getVider() {
        return vider;
    }

    public VBox getTaskList() {
        return taskList;
    }

    public Label getTaskNumber() {
        return taskNumber;
    }

    public Button getOpenMenu() {
        return openMenu;
    }

    public ToggleButton getAllBtn() {
        return allBtn;
    }

    public ToggleButton getFinishedBtn() {
        return finishedBtn;
    }

    public ToggleButton getUnfinishedBtn() {
        return unfinishedBtn;
    }

    public ScrollPane getCentralScroll() {
        return centralScroll;
    }
}
