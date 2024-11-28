module com.example.groupproject16 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.groupproject16 to javafx.fxml;
    exports com.example.groupproject16;
}