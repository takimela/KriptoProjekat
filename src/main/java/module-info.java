module com.example.kriptoprojekat {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.example.kriptoprojekat to javafx.fxml;
    exports com.example.kriptoprojekat;
}