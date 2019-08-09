package org.wh.todolist.classes;

import org.wh.materials.core.Log;

import java.io.*;
import java.util.ArrayList;

public class TaskList implements Serializable {
    private ArrayList<Task> tasks;
    private String title;

    public TaskList(String title) {
        tasks = new ArrayList<>();
        this.title = title;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean serialize(final File file) {
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
            fos.close();
        } catch (IOException e) {
            Log.writeError("Erreur surcenue lors de l'enregistrement de la task list : " + e.getMessage());
            return false;
        }
        return true;
    }

    public static TaskList deserialize(final File file) {
        TaskList taskList = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            taskList = (TaskList) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            Log.writeError("Erreur surcenue lors de l'enregistrement de la task list : " + e.getMessage());
        }
        return taskList;
    }
}
