/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wh.todolist.controllers;

import com.jfoenix.controls.JFXCheckBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.wh.todolist.classes.Task;

/**
 * @author Wilson
 */
public final class TaskView extends StackPane {
    private final Task myTask;
    private final Text myText;
    private final JFXCheckBox checkBox;

    private TaskViewSession session;

    public TaskView(Task myTask, TaskViewSession session) {
        this.myTask = myTask;
        this.session = session;

        checkBox = new JFXCheckBox();
        checkBox.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        checkBox.setSelected(myTask.isFini());

        myText = new Text(myTask.getLibelle());
        myText.setWrappingWidth(360);
        myText.setFont(Font.font("Maiandra GD", 16));
        HBox box = new HBox(checkBox,myText);
        box.setAlignment(Pos.CENTER_LEFT);
        StackPane.setMargin(box,new Insets(3,0,4,0));

        this.getChildren().addAll( box);
        this.getStyleClass().add("taskView");
        session.setTaskHovered(this);
        if (myTask.isFini()) {
            myText.getStyleClass().setAll("text-finished-task");
            myText.setStrikethrough(true);
        } else {
            session.finishedNumberProperty().set(session.getFinishedNumber() + 1);
            myText.getStyleClass().setAll("text-unfinished-task");
        }

        this.setOnMouseEntered(e -> {
            //StackPane.setAlignment(session.getBtnsBox(),Pos.CENTER_RIGHT);
            this.getChildren().add(session.getBtnsBox());
            session.setTaskHovered(this);
        });
        this.setOnMouseExited(e ->{
            this.getChildren().remove(session.getBtnsBox());
            //session.getBtnsUnderPane().close();
        });
        checkBox.selectedProperty().addListener((o, oVal, nVal) -> {
            myText.setStrikethrough(nVal);
            myTask.setFini(nVal);
            if (nVal) {
                myText.getStyleClass().setAll("text-finished-task");
                session.finishedNumberProperty().set(session.getFinishedNumber() - 1);
            } else {
                myText.getStyleClass().setAll("text-unfinished-task");
                session.finishedNumberProperty().set(session.getFinishedNumber() - 1);
            }
        });

    }

    public Task getTask() {
        return myTask;
    }

    public Text getText() {
        return myText;
    }

    public JFXCheckBox getCheckBox() {
        return checkBox;
    }

}
