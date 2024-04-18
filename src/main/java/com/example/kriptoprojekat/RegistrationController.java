package com.example.kriptoprojekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

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

        OpenSSL.genRsa(username,password);
        OpenSSL.newRequest(username,password,info);
        OpenSSL.signRequest(username);

    }

}
