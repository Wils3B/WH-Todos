/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wh.todolist.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.wh.materials.core.BackerPane;
import org.wh.materials.core.Log;
import org.wh.materials.core.Settings;
import org.wh.materials.core.UnderPane;
import org.wh.todolist.Main;
import org.wh.todolist.classes.Task;
import org.wh.todolist.classes.TaskList;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Wilson
 */
public class WindowController implements Initializable {

    @FXML
    private StackPane rootStack;

    @FXML
    private VBox mainBox;

    //for window drag
    private double dragOffsetX, dragOffsetY;
    private Stage stage;

    //Views and controllers
    private VBox centralView;
    private CentralViewController centralViewController;
    private BackerPane centralBacker;
    private VBox editTaskView;
    private EditTaskController editTaskController;
    private VBox taskListBox;
    private VBox menuView;
    private MenuViewController menuViewController;
    private UnderPane menuUnderPane;
    private VBox closeView;
    private CloseViewController closeViewController;
    private UnderPane closeUnderPane;
    private final static Label PLACEHOLDER = new Label("Pas d'élément ici.");
    private VBox saveDialogView;
    private SaveDialogController saveDialogController;
    private VBox aboutView;
    private UnderPane aboutUnderPane;
    private VBox settingsView;
    private SettingsViewController settingsViewController;

    //for code global logic
    private ArrayList<TaskView> allTaskViews;
    private int colorID;
    private TaskViewSession taskViewSession;
    private static Settings settings;

    /**
     * Initializes the controller class.
     *
     * @param url l'url
     * @param rb  la ressource
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Log.writeLog("Nouvelle session de liste! (" + this + ")");
        settings = Main.getSettings();
        stage = Main.getLastCreatedStage();

        allTaskViews = new ArrayList<>();

        loadAllViews();
        centralBacker = new BackerPane(centralView);
        mainBox.getChildren().add(1,centralBacker);
        VBox.setVgrow(centralBacker, Priority.ALWAYS);

        taskViewSession = new TaskViewSession();

        listenAllEvent();

        taskListBox = centralViewController.getTaskList();

        //UI initialization
        initializeUiComponents();

        //Opening autosaved tasklist
        if ((boolean) settings.getSetting("restoreSession"))
            openFile(new File((String) settings.getSetting("autosavePath")));
    }

    private void loadAllViews() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/centralView.fxml"));
        try {
            centralView = loader.load();
            centralViewController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //setStylesheetOfPane(centralView,"main","centralview");

        loader = new FXMLLoader(Main.class.getResource("views/editTaskView.fxml"));
        try {
            editTaskView = loader.load();
            editTaskController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //setStylesheetOfPane(editTaskView,"centralview","edittaskview");

        loader = new FXMLLoader(Main.class.getResource("views/menuView.fxml"));
        try {
            menuView = loader.load();
            menuViewController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        menuUnderPane = new UnderPane(centralBacker, menuView, Pos.TOP_RIGHT, UnderPane.UnderPaneTransition.LEFT, true);
        menuUnderPane.setExpandable(false,true);
        //setStylesheetOfPane(menuView,"menuview");

        loader = new FXMLLoader(Main.class.getResource("views/closeView.fxml"));
        try {
            closeView = loader.load();
            closeViewController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeUnderPane = new UnderPane(rootStack, closeView, Pos.CENTER, UnderPane.UnderPaneTransition.CENTER, true);
        //setStylesheetOfPane(closeView,"closeview");

        loader = new FXMLLoader(Main.class.getResource("views/saveDialogView.fxml"));
        try {
            saveDialogView = loader.load();
            saveDialogController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //setStylesheetOfPane(saveDialogView,"savedialogview","centralview");

        loader = new FXMLLoader(Main.class.getResource("views/aboutView.fxml"));
        try {
            aboutView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        aboutUnderPane = new UnderPane(centralBacker, aboutView, Pos.CENTER, UnderPane.UnderPaneTransition.CENTER, true);

        loader = new FXMLLoader(Main.class.getResource("views/settingsView.fxml"));
        try {
            settingsView = loader.load();
            settingsViewController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //setStylesheetOfPane(settingsView,"settingsview","centralview");

    }

    private void listenAllEvent() {
        //Stage event
        stage.setOnCloseRequest(event -> {
            event.consume();
            exitRequest(null);
        });

        //Central events
        centralViewController.getAddTask().setOnAction(this::addNewTask);
        centralViewController.getOpenMenu().setOnMouseClicked(event -> menuUnderPane.show(centralBacker));
        centralViewController.getMarkAll().setOnMouseClicked(event -> markAllTasks());
        centralViewController.getGroup().selectToggle(centralViewController.getAllBtn());
        centralViewController.getGroup().selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null)
                refreshTaskView((ToggleButton) newValue);
            else
                centralViewController.getGroup().selectToggle(oldValue);
        }));
        centralViewController.getVider().setOnMouseClicked(event -> clearFinishedTask());

        //Edit Task View
        editTaskController.getRetour().setOnMouseClicked(e -> centralBacker.previous());
        editTaskController.getValider().setOnMouseClicked(event -> editTaskFinished());

        //Events of task view
        taskViewSession.getDeleteBtn().setOnMouseClicked(event -> {
            event.consume();
            deleteTask(taskViewSession.getTaskHovered());
        });
        taskViewSession.getSettingBtn().setOnMouseClicked(event -> openTaskSettings(taskViewSession.getTaskHovered().getTask()));
        taskViewSession.finishedNumberProperty().addListener((observable, oldVal, newVal) -> updateNumberOfTask(newVal.intValue()));

        //Events of close dialog
        closeViewController.getQuit().setOnMouseClicked(event -> {
            settings.insertSetting("showClosedDialog", !closeViewController.getRemember().isSelected());
            close();
        });
        closeViewController.getStay().setOnMouseClicked(event -> closeUnderPane.close());
        closeViewController.getReduct().setOnMouseClicked(event -> {
            closeUnderPane.close();
            stage.setIconified(true);
        });

        //Events of menu
        menuViewController.getQuitBtn().setOnMouseClicked(this::exitRequest);
        menuViewController.getSaveBtn().setOnMouseClicked(event -> saveRequest());
        menuViewController.getOpenBtn().setOnMouseClicked(event -> openTaskList());
        menuViewController.getAboutBtn().setOnMouseClicked(this::openAbout);
        menuViewController.getSettingsBtn().setOnMouseClicked(this::openSettings);

        //Event of save dialog
        saveDialogController.getRetour().setOnMouseClicked(event -> centralBacker.previous());
        saveDialogController.getCheminBtn().setOnMouseClicked(event -> openFileSaveDialgog());
        saveDialogController.getValider().setOnMouseClicked(event -> saveTaskList());

        //Events of settings View
        settingsViewController.getRetour().setOnMouseClicked(event -> centralBacker.previous());
        settingsViewController.getValider().setOnMouseClicked(event -> {
            settingsViewController.updateSettingsFromFields();
            stage.setAlwaysOnTop((Boolean) settings.getSetting("alwaysOnTop"));
            centralBacker.previous();
        });
    }

    private void initializeUiComponents() {
        updateNumberOfTask(0);
        checkPlaceholding();
    }

    @FXML
    private void handleMousePressedOnTitleBar(MouseEvent e) {
        this.dragOffsetX = e.getScreenX() - stage.getX();
        this.dragOffsetY = e.getScreenY() - stage.getY();
    }

    @FXML
    private void handleMouseDraggedOnTitleBar(MouseEvent e) {
        stage.setX(e.getScreenX() - this.dragOffsetX);
        stage.setY(e.getScreenY() - this.dragOffsetY);
    }

    @FXML
    private void handleMousePressedOnResizer(MouseEvent e) {
        this.dragOffsetY = e.getScreenY() - stage.getHeight();
    }

    @FXML
    private void handleMouseDraggedOnResizer(MouseEvent e) {
        double h=e.getScreenY() - this.dragOffsetY;
        if(h<400)
            stage.setHeight(400);
        else
            stage.setHeight(h);
    }

    private void addNewTask(ActionEvent event) {
        Log.writeLog("Nouvelle tâche dans la session " + this);
        Task t = new Task();
        t.setLibelle(centralViewController.getAddTask().getText());
        centralViewController.getAddTask().clear();
        addTask(t);
        updateTaskSettingsFields(t);
        if ((boolean) settings.getSetting("editTaskOnCreate"))
            centralBacker.next(editTaskView);
        centralViewController.getCentralScroll().setVvalue(1);
        //centralViewController.getCentralScroll().scroll
    }

    private void addTask(Task t) {
        TaskView vue = taskViewSession.newTaskView(t);
        allTaskViews.add(vue);
        refreshTaskView((ToggleButton) centralViewController.getGroup().getSelectedToggle());
    }

    private void deleteTask(TaskView taskView) {
        int pos = taskListBox.getChildren().indexOf(taskView);
        taskListBox.getChildren().remove(pos, pos + 2);
        allTaskViews.remove(taskView);
        if (!taskView.getTask().isFini())
            taskViewSession.finishedNumberProperty().set(taskViewSession.getFinishedNumber() - 1);
    }

    private void deleteAllTask() {
        for (TaskView taskView : allTaskViews) {
            deleteTask(taskView);
        }
        allTaskViews.clear();
    }

    private void updateTaskSettingsFields(Task task) {
        editTaskController.getLibelle().setText(task.getLibelle());
        editTaskController.getDetail().setText(task.getDetails());
        DateTime d = new DateTime(task.getFin());
        editTaskController.getdFin().setValue(LocalDate.of(d.getYear(), d.getMonthOfYear(), d.getDayOfMonth()));
        editTaskController.gethFin().setValue(LocalTime.of(d.getHourOfDay(), d.getMinuteOfHour(), d.getSecondOfMinute(), 0));
    }

    private void openTaskSettings(Task t) {
        updateTaskSettingsFields(t);
        centralBacker.next(editTaskView);
    }

    private void editTaskFinished() {
        Task t = taskViewSession.getTaskHovered().getTask();
        t.setLibelle(editTaskController.getLibelle().getText());
        t.setDetails(editTaskController.getDetail().getText());
        taskViewSession.getTaskHovered().getText().setText(t.getLibelle());
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        DateTime d = DateTime.parse(editTaskController.getdFin().getValue().toString() + " " + editTaskController.gethFin().getValue().toString().substring(0, 5), formatter);
        t.setFin(d.getMillis());
        centralBacker.previous();
    }

    private void updateNumberOfTask(int a) {
        if (a <= 1) {
            centralViewController.getTaskNumber().setText(a + " tâche en cours");
        } else {
            centralViewController.getTaskNumber().setText(a + " tâches en cours");
        }
        refreshTaskView((ToggleButton) centralViewController.getGroup().getSelectedToggle());
    }

    private void markAllTasks() {
        allTaskViews.forEach(taskView -> taskView.getCheckBox().setSelected(true));
    }

    private void displayAllTask() {
        taskListBox.getChildren().clear();
        allTaskViews.forEach(taskView -> taskListBox.getChildren().addAll(taskView, new Separator()));
    }

    private void refreshTaskView(ToggleButton newValue) {
        if (taskListBox.getAlignment() == Pos.CENTER) {
            taskListBox.getChildren().clear();
            taskListBox.setAlignment(Pos.TOP_LEFT);
        }
        if (newValue == centralViewController.getAllBtn()) {
            displayAllTask();
        } else if (newValue == centralViewController.getFinishedBtn()) {
            displayFinishedTask();
        } else {
            displayUnfinishedTask();
        }
        checkPlaceholding();
    }

    private void displayFinishedTask() {
        taskListBox.getChildren().clear();
        allTaskViews.forEach(taskView -> {
            if (taskView.getTask().isFini())
                taskListBox.getChildren().addAll(taskView, new Separator());
        });
    }

    private void displayUnfinishedTask() {
        taskListBox.getChildren().clear();
        allTaskViews.forEach(taskView -> {
            if (!taskView.getTask().isFini())
                taskListBox.getChildren().addAll(taskView, new Separator());
        });
    }

    private void clearFinishedTask() {
        for (int i = taskListBox.getChildren().size() - 2; i >= 0; i -= 2) {
            TaskView taskView = (TaskView) taskListBox.getChildren().get(i);
            if (taskView.getTask().isFini()) {
                taskListBox.getChildren().remove(i, i + 2);
                allTaskViews.remove(i / 2);
            }
        }
        refreshTaskView((ToggleButton) centralViewController.getGroup().getSelectedToggle());
    }

    @FXML
    private void exitRequest(MouseEvent event) {
        if (event == null || event.getButton() == MouseButton.PRIMARY) {
            if ((boolean) settings.getSetting("showClosedDialog"))
                closeUnderPane.show();
            else
                close();
        }
    }

    private void close() {
        TaskList list = new TaskList("List autosaved");
        for (TaskView t : allTaskViews) {
            list.addTask(t.getTask());
        }
        File file = new File((String) settings.getSetting("autosavePath"));
        if (!file.exists()) {
            file.mkdirs();
        }
        list.serialize(file);

        settings.insertSetting("colorId", colorID);
        Main.removeWindowController(this);
        Log.writeLog("Fermeture de la session " + this);
        settings.insertSetting("windowHeight",stage.getHeight());
        stage.close();
    }

    private void checkPlaceholding() {
        if (taskListBox.getChildren().isEmpty()) {
            taskListBox.setAlignment(Pos.CENTER);
            taskListBox.getChildren().add(PLACEHOLDER);
        }
    }

    private void saveRequest() {
        menuUnderPane.close();
        centralBacker.next(saveDialogView);
        saveDialogController.getCheminField().setText(settings.getSetting("savePath") + "\\" + settings.getSetting("saveFile"));
        String name = (String) settings.getSetting("saveFile");
        saveDialogController.getTitre().setText(name.substring(0, name.length() - 3));
    }

    private void openFileSaveDialgog() {
        FileChooser fileDialog = createFileDialog("Enregistrer la liste");
        File file = fileDialog.showSaveDialog(stage);
        if (file == null) {
            return;
        }
        saveDialogController.getCheminField().setText(file.getAbsolutePath());
        String name = file.getName();
        saveDialogController.getTitre().setText(name.substring(0, name.length() - 3));
    }

    private void saveTaskList() {
        Log.writeLog("Enregistrement d'une liste dans la session " + this);
        TaskList list = new TaskList(saveDialogController.getTitre().getText());
        for (TaskView t : allTaskViews) {
            list.addTask(t.getTask());
        }
        File file = new File(saveDialogController.getCheminField().getText());
        list.serialize(file);
        settings.insertSetting("savePath", file.getParentFile().getAbsolutePath());
        settings.insertSetting("saveFile", file.getName());
        centralBacker.previous();
    }

    private void openTaskList() {
        menuUnderPane.close();
        FileChooser fileDialog = createFileDialog("Ouvrir une liste");
        File file = fileDialog.showOpenDialog(stage);
        if (file == null) {
            return;
        }

        openFile(file);

        menuUnderPane.close();
    }

    public void openFile(File file) {
        Log.writeLog("Ouverture d'une liste depuis un fichier dans la session : " + this);
        TaskList list = TaskList.deserialize(file);
        if (list == null) {
            return;
        }
        if (!(boolean) settings.getSetting("openAppend")) {
            deleteAllTask();
        }
        for (Task task : list.getTasks()) {
            addTask(task);
        }
    }

    private FileChooser createFileDialog(String s) {
        File openPath = new File((String) settings.getSetting("savePath"));
        if (!openPath.exists()) {
            settings.resetSetting("savePath");
            openPath = new File((String) settings.getSetting("savePath"));
        }
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle(s);
        fileDialog.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier de liste de tâches", "*.tl"));
        fileDialog.setInitialDirectory(openPath);
        fileDialog.setInitialFileName((String) settings.getSetting("saveFile"));
        return fileDialog;
    }

    @FXML
    void openAbout(MouseEvent event) {
        aboutUnderPane.show(centralBacker);
    }

    @FXML
    void openSettings(MouseEvent event) {
        if (centralBacker.getCurrent() == settingsView)
            return;
        if (menuUnderPane.isOpen())
            menuUnderPane.close();
        settingsViewController.updateFieldsFromSettings();
        centralBacker.next(settingsView);
    }

    @FXML
    void addTodoList(MouseEvent event) {
        Log.writeLog("Tentative d'ouverture d'une nouvelle session depuis " + this);
        try {
            Main.addTodoList(this, null);
        } catch (IOException e) {
            Log.writeError("Echec d'ouverture de la nouvelle session : " + e.getMessage());
        }
    }

    public int getColorID() {
        return colorID;
    }

    public void setColorID(int colorID) {
        this.colorID = colorID;
    }

    public Stage getStage() {
        return stage;
    }

    @FXML
    void mergeLists(MouseEvent event) {
        Log.writeLog("Fusion de listes en " + this);
        ArrayList<WindowController> windowControllers = Main.getWindowControllers();
        for (final WindowController windowController : windowControllers) {
            if (windowController == this) continue;
            for (TaskView t : windowController.allTaskViews) {
                addTask(t.getTask());
            }
            windowController.stage.close();
        }
        Main.removeAllWindowControllers(this);
    }

    @FXML
    void changeColor(MouseEvent event) throws NullPointerException {
        Log.writeLog("Changement de la couleur en " + this);
        colorID = Main.nextColorId(colorID);
        Log.writeLog("Nouvelle couleur: " + Main.getColorName(this));
        rootStack.getStylesheets().setAll(Main.class.getResource("views/css/" + Main.getColorName(this) + "/main.css").toExternalForm());
        rootStack.getStylesheets().add(Main.class.getResource("views/css/common/commonmain.css").toExternalForm());
    }

}
