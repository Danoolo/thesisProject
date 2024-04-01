module com.tesis.tesis {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.tesis.tesis to javafx.fxml;
    exports com.tesis.tesis;
}