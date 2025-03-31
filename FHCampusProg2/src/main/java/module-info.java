module at.ac.fhcampuswien.fhmdb {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires com.jfoenix;
    requires com.google.gson;
    opens at.ac.fhcampuswien.fhmdb.models to com.google.gson;

    exports at.ac.fhcampuswien.fhmdb;
}