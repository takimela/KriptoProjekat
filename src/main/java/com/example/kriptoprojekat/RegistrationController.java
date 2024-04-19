package com.example.kriptoprojekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

import static com.example.kriptoprojekat.Main.currentUser;

public class RegistrationController {

    @FXML
    public void goToNextScene(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("");
    }

    @FXML
    private Button buttonRegistration;

    @FXML
    private TextField textFieldRegistrationUsername;

    @FXML
    private TextField textFieldRegistrationPassword;

    @FXML
    private TextField textFieldRegistrationEmail;

    @FXML
    private TextField textFieldCountry;

    @FXML
    private TextField textFieldProvince;

    @FXML
    private TextField textFieldTown;

    @FXML
    private TextField textFieldOrganization;

    @FXML
    private TextField textFieldUnit;


    @FXML
    public void registration(ActionEvent event) throws IOException {
        String username = textFieldRegistrationUsername.getText();
        String password = textFieldRegistrationPassword.getText();
        String email = textFieldRegistrationEmail.getText();
        String country = textFieldCountry.getText();
        String province = textFieldProvince.getText();
        String town = textFieldTown.getText();
        String organization = textFieldOrganization.getText();
        String unit = textFieldUnit.getText();
        String info = "\"/C=" + country + "/ST=" + province + "/L=" + town + "/O=" + organization + "/OU=" + unit + "/CN=" + username + "/emailAddress=" + email + "\"";

        if(FileManager.isUserExists(username))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("User already exists");
            alert.setHeaderText("User with this username already exists");
            alert.setContentText("User with this username already exists, please chose another one");
            alert.showAndWait();
        } else
        {
            OpenSSL.genRsa(username,password);
            OpenSSL.newRequest(username,password,info);
            OpenSSL.signRequest(username);
            OpenSSL.createUser(username,password);

            String currDir = System.getProperty("user.dir");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration");
            alert.setHeaderText("Registration successful");
            alert.setContentText("Path to certificate file: " + currDir + "\\Data\\certs\\" + username + ".crt\n" +
                    "Path to key file: " + currDir + "\\Data\\private\\" + username + ".key");
            alert.showAndWait();

            Main m = new Main();
            m.changeScene("start-view.fxml");
        }

    }

    @FXML
    protected void handleButtonBack() throws IOException {
        Main m = new Main();
        m.changeScene("start-view.fxml");
    }

}
