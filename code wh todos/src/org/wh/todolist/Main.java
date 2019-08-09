/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wh.todolist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.wh.materials.core.CoreApplication;
import org.wh.materials.core.Log;
import org.wh.todolist.classes.core.MySettings;
import org.wh.todolist.controllers.WindowController;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Wilson
 */
public class Main extends Application {

    private static MySettings settings;
    private static ArrayList<Stage> primaryStages;
    private static ArrayList<WindowController> windowControllers;
    private static String[] colors;
    private static Stage mainStage;

    @Override
    public void start(Stage stage) {
        colors = new String[]{"blue", "yellow2", "rose", "green", "white", "yellow", "blue2", "cyan",
                "goldenrowyellow", "pink", "salmon", "seagreen", "slategrey", "steelblue", "dodgerblue"};

        //Création des ArrayLists
        primaryStages = new ArrayList<>();
        windowControllers = new ArrayList<>();
        
        Main.mainStage=stage;

        List<String> parameters = this.getParameters().getUnnamed();
        try {
            if (parameters == null || parameters.isEmpty()) {
                showMain(null);
            } else {
                showMain(parameters.get(0));
                for (int i = 1; i < parameters.size(); i++) {
                    addTodoList(windowControllers.get(i - 1), parameters.get(i));
                }
            }

        } catch (Exception e) {
            Log.writeError("Erreur, echec de chargement de la fenêtre principale");
            Log.writeError("Message de l'erreur : " + e.getMessage());
            Log.writeError("Stacktrace : " + Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void init() throws Exception {
        super.init();

        //Set the core apllication values
        CoreApplication.setApplicationName("TodoList");
        CoreApplication.setApplicationVersion("1.0");
        CoreApplication.setOrganizationDomain("");
        CoreApplication.setOrganizationName("Wilbitys House");
        CoreApplication.createAllDirs();

        //Indication des fichiers logs
        File file = new File(CoreApplication.getLogPath());
        if ((!file.exists() && file.createNewFile()) || file.exists()) {
            PrintStream ps = new PrintStream(file, "utf-8");
            Log.setNormalOutput(ps);
        }
        file = new File(CoreApplication.getErrLogPath());
        if ((!file.exists() && file.createNewFile()) || file.exists()) {
            PrintStream ps = new PrintStream(file, "utf-8");
            Log.setErrorsOutput(ps);
        }
        Log.writeLog("Ouverture de l'application");

        //load Settings
        Log.writeLog("Load settings");
        settings = new MySettings();
        settings.loadSettings();

    }

    @Override
    public void stop() throws Exception {
        //Store settings
        settings.storeSettings();

        Log.writeLog("Fermeture de l'application");
        Log.close();

        super.stop();
        System.exit(0);
    }

    /**
     * Load and display the main view
     */
    private void showMain(String fileName) throws IOException {
        primaryStages.add(mainStage);

        loadAndShow(mainStage,0,null);

        //Opening tasklist if exist
        if (fileName != null && isCorrectFile(fileName)) {
            windowControllers.get(0).openFile(new File(fileName));
        }

        addDnDGesture();
    }

    public static WindowController addTodoList(WindowController primary, String fileName) throws IOException {
        int index = primaryStages.size();
        Stage stage=new Stage();
        primaryStages.add(stage);
        stage.setX(primary.getStage().getX() + 50);
        stage.setY(primary.getStage().getY() + 50);

        boolean setting = (boolean) settings.getSetting("restoreSession");
        settings.insertSetting("restoreSession", false);

        loadAndShow(stage,index,primary);

        settings.insertSetting("restoreSession", setting);

        //Opening tasklist if exist
        if (fileName != null && isCorrectFile(fileName)) {
            windowControllers.get(index).openFile(new File(fileName));
        }

        addDnDGesture();

        return windowControllers.get(index);
    }

    private static void loadAndShow(Stage stage,int index,WindowController primary) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/windowView.fxml"));
        StackPane pane = loader.load();
        windowControllers.add(loader.getController());
        if(primary!=null)
            windowControllers.get(index).setColorID(nextColorId(primary.getColorID()));
        else{
            int colorId = (int) settings.getSetting("colorId");
            windowControllers.get(0).setColorID(colorId);
        }
        pane.getStylesheets().setAll(Main.class.getResource("views/css/" + colors[windowControllers.get(index).getColorID()] + "/main.css").toExternalForm());
        pane.getStylesheets().add(Main.class.getResource("views/css/common/commonmain.css").toExternalForm());

        stage.setScene(new Scene(pane,424, (Double) settings.getSetting("windowHeight")));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("TodoLists By WH");
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("views/images/task_48px.png")));
        stage.setMinHeight(400);
        stage.setAlwaysOnTop((Boolean) settings.getSetting("alwaysOnTop"));
        stage.show();
    }

    public static MySettings getSettings() {
        return settings;
    }

    public static Stage getLastCreatedStage() {
        return primaryStages.get(primaryStages.size() - 1);
    }

    private static Scene getLastCreatedScene() {
        return getLastCreatedStage().getScene();
    }

    public static String getColorName(WindowController windowController) {
        return colors[windowController.getColorID()];
    }

    public static ArrayList<WindowController> getWindowControllers() {
        return windowControllers;
    }

    public static void removeWindowController(WindowController windowController) {
        Log.writeLog("Suppression de la session : " + windowController);
        int index = windowControllers.indexOf(windowController);
        windowControllers.remove(index);
        primaryStages.remove(index);
    }

    public static void removeAllWindowControllers(WindowController windowController) {
        int index = windowControllers.indexOf(windowController);
        Stage stage = primaryStages.get(index);
        windowControllers.clear();
        primaryStages.clear();
        windowControllers.add(windowController);
        primaryStages.add(stage);
    }

    public static int nextColorId(int colorId) {
        int plus = (int) (Math.random() * colors.length);
        return (colorId + plus) % colors.length;
    }

    private static boolean isCorrectFile(String name) {
        if (name.endsWith(".tl")) {
            File file = new File(name);
            return file.exists();
        }
        return false;
    }

    private static void addDnDGesture(){
        Scene scene=getLastCreatedScene();
        scene.setOnDragOver(Main::dragOver);
        scene.setOnDragDropped(Main::dragDropped);
    }

    private static void dragOver(DragEvent event){
        Dragboard dragboard=event.getDragboard();

        if (dragboard.hasFiles() || dragboard.hasUrl()){
            event.acceptTransferModes(TransferMode.ANY);
        }

        event.consume();
    }

    private static void dragDropped(DragEvent event) {
        Log.writeLog("Ouverture de fichiers par drag n drop");
        Dragboard dragboard=event.getDragboard();
        WindowController last=windowControllers.get(windowControllers.size()-1);
        if (dragboard.hasUrl()){
            String[] urls=dragboard.getUrl().split("[\\n]");
            boolean actualHasOpened=false;
            for(String str:urls){
                try {
                    URL url=new URL(str);
                    String path=url.getPath();
                    if (isCorrectFile(path)){
                        if (actualHasOpened){
                            last=addTodoList(last,path);
                        }else{
                            last.openFile(new File(path));
                            actualHasOpened=true;
                        }
                    }
                } catch (MalformedURLException e) {
                    Log.writeError("URL MAL FORMEE : "+e.getMessage());
                } catch (IOException e){
                    Log.writeError("Echec d'ouverture du fichier par drag n drop");
                }
            }
        } else {
            List<File> files=dragboard.getFiles();
            System.out.println(files);
            boolean actualHasOpened=false;
            for (File file:files){
                if (isCorrectFile(file.getAbsolutePath())){
                    if(actualHasOpened){
                        try {
                            last=addTodoList(last,file.getAbsolutePath());
                        } catch (IOException e) {
                            Log.writeError("Echec lors de l'ouverture du fichier par drag n drop : "+e.getMessage());
                        }
                    }else{
                        last.openFile(file);
                        actualHasOpened=true;
                    }
                }
            }
        }

        event.consume();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
