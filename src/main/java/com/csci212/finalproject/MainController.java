package com.csci212.finalproject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainController implements Initializable {

    private NewsAPICall news;
    private weatherAPICall weather; // Keep separate for setting page
    private stockAPICall stocks;

    /* News Panel Components */
    @FXML
    public VBox newsTextBox;

    /* Weather Panel Components */
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

    /* Stock Panel Components */
    @FXML
    private Label symbol;
    @FXML
    private Label currentPrice;
    @FXML
    private Label change;
    @FXML
    private Label percentChange;
    @FXML
    private ImageView stockArrow;
    @FXML
    private Label symbol1;
    @FXML
    private Label currentPrice1;
    @FXML
    private Label change1;
    @FXML
    private Label percentChange1;
    @FXML
    private ImageView stockArrow1;
    @FXML
    private Label symbol2;
    @FXML
    private Label currentPrice2;
    @FXML
    private Label change2;
    @FXML
    private Label percentChange2;
    @FXML
    private ImageView stockArrow2;
    @FXML
    private Button dailyTasksButton;
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

    @FXML
    private void tasksButtonPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DailyTasks.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Daily Tasks");
        stage.show();
    }

    public void initialize(URL arg0, ResourceBundle arg1) {
        weather = new weatherAPICall();
        news = new NewsAPICall();
        news.getNewsHeadline();
        stocks = new stockAPICall();

        if (news.articlesList == null || news.isNull()) {
            Label noNewsLabel = new Label("No news available by search criteria.");
            noNewsLabel.setStyle("-fx-alignment: center; -fx-font-weight: bold; -fx-font-size: 18; -fx-text-alignment: center; -fx-content-display: center;");
            noNewsLabel.setMaxWidth(Double.MAX_VALUE);
            noNewsLabel.setMaxHeight(Double.MAX_VALUE);
            newsTextBox.getChildren().add(noNewsLabel);
        } else {
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

        dailyTasksButton.setOnAction(event -> {
            try {
                tasksButtonPressed(event);
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


        List<StockData> allStockData = stocks.getAllStockData();

        for (int i = 0; i < allStockData.size(); i++) {
            StockData stockData = allStockData.get(i);

            if (i == 0) {
                updateStockLabels(symbol, currentPrice, change, percentChange, stockArrow, stockData);
            } else if (i == 1) {
                updateStockLabels(symbol1, currentPrice1, change1, percentChange1, stockArrow1, stockData);
            } else if (i == 2) {
                updateStockLabels(symbol2, currentPrice2, change2, percentChange2, stockArrow2, stockData);
            }
        }
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
            if (url == null || url.isEmpty()) {
                logger.warning("Invalid URL: URL is empty or null");
                return;
            }

            try {
                java.net.URI uri = new java.net.URI(url);
                if (!java.awt.Desktop.isDesktopSupported() ||
                        !java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)
                ) {
                    logger.warning("Desktop browsing not supported on this platform");
                    return;
                }

                java.awt.Desktop.getDesktop().browse(uri);
            } catch (java.net.URISyntaxException ex) {
                logger.severe("Invalid URI syntax: " + ex.getMessage());
            } catch (java.io.IOException ex) {
                logger.severe("Error opening browser: " + ex.getMessage());
            }

        });
        return link;
    }

    private void updateStockLabels(Label symbolLabel, Label priceLabel, Label changeLabel, Label percentChangeLabel, ImageView arrow, StockData stockData) {
        symbolLabel.setText(stockData.getSymbol());
        priceLabel.setText(String.format("$%.2f", stockData.getCurrentPrice()));
        changeLabel.setText(String.format("$%.2f", stockData.getChange()));
        percentChangeLabel.setText(String.format("%.2f%%", stockData.getPercentChange()));

        if (stockData.getPercentChange() > 0) {
            try {
                arrow.setImage(new Image(new FileInputStream("src/main/resources/com/csci212/finalproject/greenArrow.png")));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            percentChangeLabel.setTextFill(Color.GREEN);
            changeLabel.setTextFill(Color.GREEN);

        } else {
            try {
                arrow.setImage(new Image(new FileInputStream("src/main/resources/com/csci212/finalproject/redArrow.png")));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            percentChangeLabel.setTextFill(Color.RED);
            changeLabel.setTextFill(Color.RED);
        }
    }

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainController.class.getName());
}

