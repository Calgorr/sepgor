module com.example.sepgorfront01 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires gson;
    requires java.sql;

    opens com.example.sepgorfront01 to javafx.fxml;
    opens pkg to gson;
    exports com.example.sepgorfront01;

}