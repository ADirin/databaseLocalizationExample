module org.example.dbdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.dbdemo to javafx.fxml;
    exports org.example.dbdemo;
}