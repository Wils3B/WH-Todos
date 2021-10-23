module org.wh.todolist {
    requires java.logging;
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    requires org.joda.time;
    requires com.jfoenix;

    requires org.wh.materials.core;

    opens org.wh.todolist to javafx.fxml;
    exports org.wh.todolist;
    exports org.wh.todolist.controllers to javafx.fxml;
    opens org.wh.todolist.controllers to javafx.fxml;
}