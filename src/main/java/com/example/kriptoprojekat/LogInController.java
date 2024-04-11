package com.example.kriptoprojekat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LogInController {
    @FXML
    private Label wrongInfo;

    @FXML
    protected void handleButtonLogIn() { wrongInfo.setText("Nema nicega"); }
}
