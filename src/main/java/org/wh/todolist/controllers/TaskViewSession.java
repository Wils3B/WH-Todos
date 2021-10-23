package org.wh.todolist.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.wh.materials.core.UnderPane;
import org.wh.todolist.Main;
import org.wh.todolist.classes.Task;

public class TaskViewSession {
    private Button settingBtn;
    private TaskView taskHovered;
    private Button deleteBtn;
    private SimpleIntegerProperty finishedNumber;
    private HBox btnsBox;

    public TaskViewSession() {
        settingBtn = new Button();
        settingBtn.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("views/images/settings_24px.png"))));
        deleteBtn = new Button();
        ImageView icone = new ImageView(new Image(Main.class.getResourceAsStream("views/images/delete_sign_48px.png")));
        icone.setFitHeight(24);
        icone.setFitWidth(24);
        deleteBtn.setGraphic(icone);
        finishedNumber = new SimpleIntegerProperty(0);
        settingBtn.setTooltip(new Tooltip("Modifier/Afficher en détails la tâche"));
        deleteBtn.setTooltip(new Tooltip("Supprimer la tâche\nAttention vous ne pouvez pas annuler!"));

        btnsBox=new HBox(settingBtn,deleteBtn);
        StackPane.setAlignment(btnsBox,Pos.CENTER_RIGHT);
        btnsBox.getStyleClass().add("btns-box");
        btnsBox.setSpacing(8);
    }

    public Button getSettingBtn() {
        return settingBtn;
    }

    public TaskView getTaskHovered() {
        return taskHovered;
    }

    public void setTaskHovered(TaskView taskHovered) {
        this.taskHovered = taskHovered;
    }

    public Button getDeleteBtn() {
        return deleteBtn;
    }

    public SimpleIntegerProperty finishedNumberProperty() {
        return finishedNumber;
    }

    public int getFinishedNumber() {
        return finishedNumber.get();
    }

    public TaskView newTaskView(Task task) {
        return new TaskView(task, this);
    }

    public HBox getBtnsBox() {
        return btnsBox;
    }
}
