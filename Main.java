import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Daily Personal Recap");
        frame.setLayout(new GridLayout(6, 1, 5, 5));

        // Create message label
        JLabel messageLabel = new JLabel("Welcome to your Daily Personal Recap", SwingConstants.CENTER);

        // Create panel for news source selection
        JPanel sourcePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel sourceLabel = new JLabel("Select News Source:");
        String[] options = {"Please select...", "Option 1", "Option 2", "Option 3"};
        JComboBox<String> dropdown = new JComboBox<>(options);
        dropdown.setSelectedIndex(0);
        sourcePanel.add(sourceLabel);
        sourcePanel.add(dropdown);
/*
        // Add action listener to dropdown
        dropdown.addActionListener(e -> {
            String selectedOption = (String) dropdown.getSelectedItem();
            // Perform actions based on selected option
            switch (selectedOption) {
                case "Option 1" -> {
                    // Perform action for Option 1
                }
                case "Option 2" -> {
                    // Perform action for Option 2
                }
                case "Option 3" -> {
                    // Perform action for Option 3
                }
            }
        });
*/
        // Add text field for user zip code
        JTextField zipCodeField = new JTextField(10);
        zipCodeField.setToolTipText("Enter your zip code");

        /*
        // Add action listener to zip code field
        zipCodeField.addActionListener(e -> {
            String zipCode = zipCodeField.getText().trim();
        });
        */


        // Add label for zip code field
        JLabel zipCodeLabel = new JLabel("Zip Code:");
        zipCodeLabel.setLabelFor(zipCodeField);

        // Add text field for user stock ticker
        JTextField stockTickerField = new JTextField(10);
        stockTickerField.setToolTipText("Enter the stock ticker");

        /*
        // Add action listener to stock ticker field
        stockTickerField.addActionListener(e -> {
            String stockTicker = stockTickerField.getText().trim();
        });
        */

        // Add label for stock ticker field
        JLabel stockTickerLabel = new JLabel("Stock Ticker:");

        // Create continue button with preferred size
        JButton continueButton = new JButton("Continue");
        continueButton.setPreferredSize(new Dimension(100, 30));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(continueButton);
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform action when continue button is clicked

                String selectedOption = (String) dropdown.getSelectedItem();
                String zipCode = zipCodeField.getText().trim();
                String stockTicker = stockTickerField.getText().trim();

                if (selectedOption.equals("Please select...") || zipCode.isEmpty() || stockTicker.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please complete all fields before continuing.",
                            "Missing Information",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                //String news = NewsAPICall.
                //String weather = weatherAPICall.getWeather(zipCode);
                //String stock = stockAPICall.getStockData(stockTicker);

                // Create a new frame for the recap page
                JFrame infoFrame = new JFrame("Recap Page");
                infoFrame.setLayout(new BorderLayout());

                JTextArea infoText = new JTextArea();
                infoText.setEditable(false);
                infoText.setText("Weather: " + /*weather +*/ "\nStock: " /*+ stock*/);

                infoFrame.add(new JScrollPane(infoText), BorderLayout.CENTER);

                infoFrame.setSize(500,300);
                infoFrame.setLocationRelativeTo(null);
                infoFrame.setVisible(true);

            }
        });

        // Add components to frame
        frame.add(messageLabel);
        frame.add(sourcePanel);
        
        // Panel for zip code (label and field in one row)
        JPanel zipPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        zipPanel.add(zipCodeLabel);
        zipPanel.add(zipCodeField);
        frame.add(zipPanel);
        
        // Panel for stock ticker (label and field in one row)
        JPanel stockPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stockPanel.add(stockTickerLabel);
        stockPanel.add(stockTickerField);
        frame.add(stockPanel);
        
        frame.add(buttonPanel);

        // Configure frame
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}