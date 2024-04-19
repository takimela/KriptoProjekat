package com.example.kriptoprojekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

import static com.example.kriptoprojekat.Main.currentUser;

public class StartPageController {
    @FXML
    private Label labelWrongCertificate;

    @FXML
    private TextField textFieldCertificate;

    @FXML
    protected void handleButtonLogIn() { labelWrongCertificate.setText("Nema nicega"); }

    @FXML
    public void goToRegistrationView(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("registration-view.fxml");
    }

    @FXML
    public void goToLoginView(ActionEvent event) throws IOException {

        String certificate = textFieldCertificate.getText();

        if(!FileManager.isCertificateExists(certificate))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Certificate doesn't exist");
            alert.setHeaderText("Certificate doesn't exist");
            alert.setContentText("Certificate with this name doesn't exist, please try again");
            alert.showAndWait();
        } else if (!OpenSSL.isValidCertTime(certificate))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Certificate has expired");
            alert.setHeaderText("Certificate has expired");
            alert.setContentText("Certificate has expired, please contact the competent authority");
            alert.showAndWait();
        } else if (!OpenSSL.isValidCertificate(certificate))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Verification filed");
            alert.setHeaderText("Verification filed, certificate revoked");
            alert.setContentText("Certificate revoked, please contact the competent authority");
            alert.showAndWait();
        } else
        {
            currentUser = certificate.substring(0, certificate.lastIndexOf("."));

            Main m = new Main();
            m.changeScene("login-view.fxml");
        }
    }
}
