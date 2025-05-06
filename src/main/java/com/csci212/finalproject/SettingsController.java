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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    //Zip code
    @FXML
    private TextField zipCode;

    //Stock input
    @FXML
    private TextField stockBox;

    //Country box
    @FXML
    private ComboBox<String> newsCountryBox;
    ObservableList<String> country = FXCollections.observableArrayList(
    "Argentina","Australia","Austria","Belgium","Brazil","Canada","Chile","China","Columbia","Cuba","Czech",
        "Egypt","France","Germany","Greece","Hong Kong","Hungary","India","Indonesia","Ireland","Israel","Italy","Japan",
        "Latvia", "Lithuania","Malaysia","Mexico","Morocco","Netherlands","New Zealand","Nigeria","Norway","Philippines",
        "Poland","Portugal","Romania","Russia","Saudi Arabia","Serbia","Singapore","Slovakia","Slovenia","South Africa",
        "South Korea","Sweden","Switzerland","Taiwan","Thailand","Turkey","UAE", "Ukraine","United Kingdom",
        "United States","Venezuela"
    );
    //Hashmap of all countries that are offered by the API
    private static final Map<String, String> countryMap = new HashMap<>();
    static {
        countryMap.put("Argentina", "ar");
        countryMap.put("Australia", "au");
        countryMap.put("Austria", "at");
        countryMap.put("Belgium", "be");
        countryMap.put("Brazil","br");
        countryMap.put("Canada", "ca");
        countryMap.put("China","cn");
        countryMap.put("Colombia", "co");
        countryMap.put("Cuba","cu");
        countryMap.put("Czech Republic", "cz");
        countryMap.put("Egypt","eg");
        countryMap.put("France","fr");
        countryMap.put("Germany", "de");
        countryMap.put("Greece", "gr");
        countryMap.put("Hong Kong", "hk");
        countryMap.put("Hungary", "hu");
        countryMap.put("India", "in");
        countryMap.put("Indonesia", "id");
        countryMap.put("Ireland", "ir");
        countryMap.put("Israel","il");
        countryMap.put("Italy","it");
        countryMap.put("Japan", "jp");
        countryMap.put("Latvia", "lv");
        countryMap.put("Lithuania", "lt");
        countryMap.put("Malaysia","my");
        countryMap.put("Mexico", "mx");
        countryMap.put("Morocco", "ma");
        countryMap.put("Netherlands","nl");
        countryMap.put("New Zealand", "nz");
        countryMap.put("Nigeria","ng");
        countryMap.put("Norway","no");
        countryMap.put("Philippines","ph");
        countryMap.put("Poland", "pl");
        countryMap.put("Portugal", "pt");
        countryMap.put("Romania", "ro");
        countryMap.put("Russia","ru");
        countryMap.put("Saudi Arabia", "sa");
        countryMap.put("Serbia","rs");
        countryMap.put("Singapore","sg");
        countryMap.put("Slovakia","sl");
        countryMap.put("Slovenia", "si");
        countryMap.put("South Africa","za");
        countryMap.put("South Korea","kr");
        countryMap.put("Sweden", "se");
        countryMap.put("Switzerland", "ch");
        countryMap.put("Taiwan","tw");
        countryMap.put("Thailand","th");
        countryMap.put("Turkey","tr");
        countryMap.put("UAE","ae");
        countryMap.put("Ukraine","ua");
        countryMap.put("United Kingdom","gb");
        countryMap.put("United States","us");
        countryMap.put("Venezuela","ve");
    }

    //Category box
    @FXML
    private ComboBox<String> categoryBox;
    ObservableList<String> category = FXCollections.observableArrayList(
        "None","Business","Entertainment","General","Health","Science","Sports","Technology"
    );

    //Save
    @FXML
    private Button saveButton;

    @SuppressWarnings("unchecked")
    private void saveSettings(ActionEvent event) throws IOException{
        if (!checkFields()) { //Checks if all fields are filled
            showAlert("Empty Fields", "All fields must be input",
                    "Please fill in all fields."
            );
            return;
        }

        String zipCodeText = zipCode.getText();
        if (!isValidZipCode(zipCodeText)) {
            showAlert("Invalid Zip Code", "Invalid Zip Code",
                    "Please enter a valid 5-digit zip code."
            );
            return;
        }

        JSONObject settings = new JSONObject();
        settings.put("zipCode", zipCode.getText());

        String newsCountry = countryMap.get(newsCountryBox.getValue());
        settings.put("newsCountry", newsCountry);
        settings.put("newsCategory", categoryBox.getValue());

        String input = stockBox.getText().toUpperCase();
        String[] tickers = input.split("\\s*,\\s*");
        JSONArray tickerArray = new JSONArray();
        for (String ticker : tickers) {
            tickerArray.add(ticker);
        }
        settings.put("stockTicker", tickerArray);

        try (FileWriter file = new FileWriter("src/main/resources/com/csci212/finalproject/settings.json")) {
            file.write(settings.toJSONString());
            file.flush();
            System.out.println("Settings saved!");
        } catch (IOException e) {
            logger.severe("Error writing to settings file: " + e.getMessage());
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
                logger.severe("Error saving settings: " + e.getMessage());
            }
        });

        categoryBox.setItems(category);
        categoryBox.getSelectionModel().select("None");
        newsCountryBox.setItems(country);
        newsCountryBox.getSelectionModel().select("United States");

    }

    private boolean checkFields() {
        return !(zipCode.getText().isEmpty() ||
                stockBox.getText().isEmpty()
        );
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

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SettingsController.class.getName());
}
