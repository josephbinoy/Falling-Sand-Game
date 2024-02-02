module com.fallingsand {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.fallingsand to javafx.fxml;
    exports com.fallingsand;
}
