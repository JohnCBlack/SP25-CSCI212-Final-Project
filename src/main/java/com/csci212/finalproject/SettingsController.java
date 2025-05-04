package com.csci212.finalproject;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    private TextField newsTextBox;

    //Populate combo box with API country list
    @FXML
    private ComboBox<String> newsCountryBox;
    public final static String[] countryArray = {
            "Argentina","Australia","Austria","Belgium","Brazil","Canada","Chile","China","Columbia","Cuba","Czech",
            "Egypt","France","Germany","Greece","Hong Kong","Hungary","India","Indonesia","Ireland","Israel","Italy","Japan",
            "Latvia", "Lithuania","Malaysia","Mexico","Morocco","Netherlands","New Zealand","Nigeria","Norway","Philippines","Poland","Portugal","Romania",
            "Russia","Saudi Arabia","Serbia","Singapore","Slovakia","Slovenia","South Africa","South Korea","Sweden","Switzerland","Taiwan","Thailand","Turkey","UAE",
            "Ukraine","United Kingdom","United States","Venezuela"
    };
    ObservableList<String> country = FXCollections.observableArrayList(countryArray);

    @FXML
    private ComboBox<String> languageBox;
    public final static String[] languageArray = {
            "Arabic", "German","English","Spanish","French","Hebrew","Italian","Dutch","Norwegian","Portuguese", "Russian","Swedish","Universal Dependencies","Chinese"
    };
    ObservableList<String> language = FXCollections.observableArrayList(languageArray);

    @FXML
    private ComboBox<String> categoryBox;
    public final static String[] categoryArray = {
            "business","entertainment","general","health","science","sports","technology",
    };
    ObservableList<String> category = FXCollections.observableArrayList(categoryArray);

    @FXML
    private Button saveButton;

    public void saveSettings(ActionEvent event) throws IOException{
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

        categoryBox.setItems(category);
        languageBox.setItems(language);
        newsCountryBox.setItems(country);

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
