module com.csci212.finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires java.desktop;
    requires java.logging;


    opens com.csci212.finalproject to javafx.fxml;
    exports com.csci212.finalproject;
}