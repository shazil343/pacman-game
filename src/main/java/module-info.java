module com.example.groupproject16 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.groupproject16 to javafx.fxml;
    exports com.example.groupproject16;
}