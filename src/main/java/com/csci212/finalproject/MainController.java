package com.csci212.finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private NewsAPICall news;
    private weatherAPICall weather; // Keep separate for setting page
    private stockAPICall stocks;

    @FXML
    public VBox newsTextBox;
    @FXML
    private Label conditionLabel;
    @FXML
    private ImageView weatherIcon;
    @FXML
    private Label currentTemp;
    @FXML
    private Label maxTemp;
    @FXML
    private Label minTemp;
    @FXML
    private Label changeOfPercp;
    @FXML
    private Label symbol;
    @FXML
    private Label currentPrice;
    @FXML
    private Label previousClosePrice;
    @FXML
    private Label change;
    @FXML
    private Label percentChange;
    @FXML
    private Label high;
    @FXML
    private Label low;
    @FXML
    private Label open;

    //Settings
    @FXML
    private Button settingsButton;
    private void settingsButtonPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Settings.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize(URL arg0, ResourceBundle arg1) {
        weather = new weatherAPICall();
        news = new NewsAPICall();
        news.getNewsHeadline();
        stocks = new stockAPICall();

        if (news.articlesList.isEmpty()) {
            Label noNewsLabel = new Label("No news available by search criteria.");
            noNewsLabel.setStyle("-fx-alignment: center; -fx-font-weight: bold; -fx-font-size: 18; -fx-text-alignment: center; -fx-content-display: center;");
            noNewsLabel.setMaxWidth(Double.MAX_VALUE);
            noNewsLabel.setMaxHeight(Double.MAX_VALUE);
            newsTextBox.getChildren().add(noNewsLabel);
        }
        for (ArrayList<String> article : news.articlesList) {
            Hyperlink link = getHyperlink(article);

            Label author = new Label(article.get(1));
            author.setWrapText(true);
            author.setPrefWidth(680);
            author.setStyle("-fx-padding: 0 5 0 5;");

            Label description = new Label(article.get(2));
            description.setWrapText(true);
            description.setPrefWidth(680);
            description.setStyle("-fx-padding: 0 5 5 5; -fx-font-style: italic;");


            newsTextBox.getChildren().add(link);
            newsTextBox.getChildren().add(author);
            newsTextBox.getChildren().add(description);

            Separator separator = new Separator();
            separator.setPrefWidth(680);
            newsTextBox.getChildren().add(separator);
        }



        try {
            Image settingsIcon = new Image(new FileInputStream("src/main/resources/com/csci212/finalproject/settings-icon.png"));
            ImageView settingsIconView = new ImageView(settingsIcon);
            settingsIconView.setFitHeight(25);
            settingsIconView.setPreserveRatio(true);
            settingsButton.setGraphic(settingsIconView);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        settingsButton.setOnAction(event -> {
            try {
                settingsButtonPressed(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Image weatherIconLink = new Image(
                String.format("http:%s", weather.getIcon())
        );

        conditionLabel.setText(weather.getCondition());
        weatherIcon.setImage(weatherIconLink); //set image
        currentTemp.setText(
                String.format("%.1f°F", weather.getCurrentTemp())
        );
        maxTemp.setText(
                String.format("%.1f°F", weather.getMaxTemp())
        );
        minTemp.setText(
                String.format("%.1f°F", weather.getMinTemp())
        );
        changeOfPercp.setText(
                String.format("%.1f%%", weather.getChangeOfRain())
        );

        currentPrice.setText(
                String.format("$%.2f", stocks.getCurrentPrice())
        );
        previousClosePrice.setText(
                String.format("$%.2f", stocks.getPreviousClose())
        );
        change.setText(
                String.format("$%.2f", stocks.getChange())
        );
        percentChange.setText(
                String.format("%.2f%%", stocks.getPercentChange())
        );
        high.setText(
                String.format("$%.2f", stocks.getHigh())
        );
        low.setText(
                String.format("$%.2f", stocks.getLow())
        );
        open.setText(
                String.format("$%.2f", stocks.getOpen())
        );
        symbol.setText(stocks.getStockTicker());

    }

    private static Hyperlink getHyperlink(ArrayList<String> article) {
        String title = article.get(0);  // title is at index 0
        String url = article.get(3);    // url is at index 3

        Hyperlink link = new Hyperlink(title);
        link.setWrapText(true);
        link.setPrefWidth(680); // Set width just slightly less than the ScrollPane width
        link.setStyle("-fx-padding: 5 5 5 5;");
        link.setWrapText(true);
        link.setOnAction(e -> {
            try {
                java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return link;
    }
}