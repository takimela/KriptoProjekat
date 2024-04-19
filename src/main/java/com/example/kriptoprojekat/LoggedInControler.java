package com.example.kriptoprojekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.kriptoprojekat.Main.*;

public class LoggedInControler implements Initializable {

    private String history = "";

    @FXML
    private TextArea textAreaEnc;

    @FXML
    private TextArea textAreaInput;

    @FXML
    private TextField textFieldKey;

    @FXML
    private ComboBox<String> comboBoxAlgorithm;

    @FXML
    private ComboBox<String> comboBoxRails;

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        comboBoxRails.getItems().addAll("2", "3", "4", "5");
        comboBoxAlgorithm.getItems().addAll("Rail Fence", "Myszkowski", "Play Fair");

        comboBoxAlgorithm.setValue("Rail Fence");
        comboBoxRails.setValue("3");

        textAreaInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 100) {
                textAreaInput.setText(newValue.substring(0, 100));
            }
        });

        history = FileManager.readSimulations(loggedInUser, loggedInPassword);

        if(!OpenSSL.verifySignature(loggedInUser, loggedInPassword)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Warning");
            alert.setContentText("History verification failed, the content of the history was illegally changed!");
            alert.showAndWait();
        }

    }

    public void encrypt(ActionEvent actionEvent) {
        String algorithm = comboBoxAlgorithm.getValue();
        String key = textFieldKey.getText();
        String rails = comboBoxRails.getValue();
        String text = textAreaInput.textProperty().get();

        if(text.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Insert text");
            alert.setHeaderText("Insert text");
            alert.setContentText("Enter the text you want to encrypt!");
            alert.showAndWait();
        } else if (algorithm.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Chose algorithm");
            alert.setHeaderText("Chose algorithm");
            alert.setContentText("Enter the algorithm you want to use!");
            alert.showAndWait();
        } else if (algorithm.equals("Rail Fence") && rails.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Chose number of rails");
            alert.setHeaderText("Chose number of rails");
            alert.setContentText("Enter the number of rails you want to use!");
            alert.showAndWait();
        } else if (!algorithm.equals("Rail Fence") && key.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Insert key");
            alert.setHeaderText("Insert key");
            alert.setContentText("Enter the key you want to use!");
            alert.showAndWait();
        } else
        {
            String simulation = text + " | " + algorithm + " | ";

            if(algorithm.equals("Rail Fence"))
            {
                textAreaEnc.setText(Algorithms.railFence(text.replaceAll("\\s", ""),Integer.parseInt(rails)));
                simulation += rails;
            } else if (algorithm.equals("Myszkowski"))
            {
                textAreaEnc.setText(Algorithms.myszkowski(key,text));
                simulation += key;
            } else if (algorithm.equals("Play Fair"))
            {
                textAreaEnc.setText(Algorithms.playFair(key,text));
                simulation += key;
            }

            simulation += " | " + textAreaEnc.getText();
            history +=(simulation + "\n");
            FileManager.writeSimulation(history.trim(), loggedInUser, loggedInPassword);
        }

    }
    public void showHistory(ActionEvent actionEvent) {
        textAreaEnc.setText(history);
    }
    public void logOut(ActionEvent actionEvent) throws IOException {
        loggedInUser = "";
        currentUser = "";
        loggedInPassword = "";

        Main m = new Main();
        m.changeScene("start-view.fxml");

    }
}
