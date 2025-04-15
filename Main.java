import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Daily Personal Recap");
        frame.setLayout(new FlowLayout());

        // Create label for dropdown
        JLabel sourceLabel = new JLabel("Select news source:");

        // Create message label
        JLabel messageLabel = new JLabel("Welcome to your Daily Personal Recap");

        // Create dropdown menu
        String[] options = {"Please select...", "Option 1", "Option 2", "Option 3"};
        JComboBox<String> dropdown = new JComboBox<>(options);
        dropdown.setSelectedIndex(0);

        // Add action listener to dropdown
        dropdown.addActionListener(e -> {
            String selectedOption = (String) dropdown.getSelectedItem();
            // Perform actions based on selected option
            if (selectedOption.equals("Option 1")) {
                // Perform action for Option 1
            } else if (selectedOption.equals("Option 2")) {
                // Perform action for Option 2
            } else if (selectedOption.equals("Option 3")) {
                // Perform action for Option 3
            }
        });

        // Create continue button
        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform action when continue button is clicked
            }
        });

        // Add components to frame
        frame.add(messageLabel);
        frame.add(sourceLabel);
        frame.add(dropdown);
        frame.add(continueButton);

        // Configure frame
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}