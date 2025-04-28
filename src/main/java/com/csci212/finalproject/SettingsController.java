package com.csci212.finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    Stage stage = new Stage();

    @FXML
    private TextField zipCode;

    @FXML
    private Button saveButton;
    private void saveSettings(ActionEvent event) {
        JSONObject settings = new JSONObject();
        settings.put("zipCode", zipCode.getText());

        try (FileWriter file = new FileWriter("src/main/resources/com/csci212/finalproject/settings.json")) {
            file.write(settings.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.close();
    }

    public void initialize(URL arg0, ResourceBundle arg1) {
        saveButton.setOnAction(this::saveSettings);
    }
}
