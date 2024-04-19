package com.example.kriptoprojekat;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

import static com.example.kriptoprojekat.Main.*;

public class LogInController {
    @FXML
    private TextField textFieldLoginUsername;

    @FXML
    private TextField textFieldLoginPassword;

    @FXML
    protected void handleButtonLogIn() throws IOException {
        String username = textFieldLoginUsername.getText();
        String password = textFieldLoginPassword.getText();

        if (!username.equals(currentUser)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong username");
            alert.setHeaderText("Wrong username");
            alert.setContentText("The username you entered don't match with one in the certificate!");
            alert.showAndWait();
        } else if(!FileManager.isUserExists(username))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("User doesn't exist");
            alert.setHeaderText("User doesn't exist");
            alert.setContentText("The username you entered doesn't exist!");
            alert.showAndWait();
        } else if(!OpenSSL.isValidPasswort(username, password))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong password");
            alert.setHeaderText("Wrong password");
            alert.setContentText("The password you entered is incorrect!");
            alert.showAndWait();
        } else
        {
            loggedInUser = username;
            loggedInPassword = password;

            Main m = new Main();
            m.changeScene("loggedin-view.fxml");
        }
    }

    @FXML
    protected void handleButtonBack() throws IOException {
        currentUser = "";

        Main m = new Main();
        m.changeScene("start-view.fxml");
    }
}
