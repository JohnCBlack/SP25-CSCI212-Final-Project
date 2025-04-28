package com.csci212.finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    @FXML
    private TextField zipCode;

    @FXML
    private Button saveButton;
    private void saveSettings(ActionEvent event) throws IOException{
        String zipCodeText = zipCode.getText();
        if (!isValidZipCode(zipCodeText)) {
            showAlert("Invalid Zip Code", "Invalid Zip Code",
                    "Please enter a valid 5-digit zip code."
            );
            return;
        }

        JSONObject settings = new JSONObject();
        settings.put("zipCode", zipCode.getText());

        try (FileWriter file = new FileWriter("src/main/resources/com/csci212/finalproject/settings.json")) {
            file.write(settings.toJSONString());
            file.flush();
            System.out.println("Settings saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize(URL arg0, ResourceBundle arg1) {
        saveButton.setOnAction(event -> {
            try {
                saveSettings(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean isValidZipCode(String zipCode) {
        return zipCode != null && zipCode.matches("\\d{5}(-\\d{4})?");
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
