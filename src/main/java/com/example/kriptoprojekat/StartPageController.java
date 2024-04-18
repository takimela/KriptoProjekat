package com.example.kriptoprojekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class StartPageController {
    @FXML
    private Label labelWrongCertificate;

    @FXML
    protected void handleButtonLogIn() { labelWrongCertificate.setText("Nema nicega"); }

    @FXML
    public void goToRegistrationView(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("registration-view.fxml");
    }

    @FXML
    public void goToLoginView(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("login-view.fxml");
    }
}
