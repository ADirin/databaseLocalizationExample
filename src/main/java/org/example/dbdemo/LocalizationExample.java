package org.example.dbdemo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox; // Import for language selection
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationExample extends Application {

    //Declare Class_level variables for the resource bundle and UI components:

    private ResourceBundle bundle;
    private Label firstNameLabel;
    private Label lastNameLabel;
    private Label emailLabel;
    private Button saveButton;

    //Override the start method inherited from Application
    @Override
    public void start(Stage primaryStage) {
        //create a ComboBox for language selection:
        ComboBox<String> languageSelector = new ComboBox<>(); // Language selection dropdown
        languageSelector.getItems().addAll("English", "Farsi", "Japanese");
        languageSelector.setValue("English"); // Default language

        //Set language selection event handler:
        languageSelector.setOnAction(event -> {
            String selectedLanguage = languageSelector.getValue();
            if (selectedLanguage.equals("Farsi")) {
                bundle = ResourceBundle.getBundle("messages", new Locale("fa", "IR")); // Farsi (Iran)
            } else if (selectedLanguage.equals("Japanese")) {
                bundle = ResourceBundle.getBundle("messages", Locale.JAPAN); // Japanese (Japan)
            } else {
                bundle = ResourceBundle.getBundle("messages", Locale.ENGLISH); // Default to English
            }
             // Update UI components based on the selected language
            updateUI(primaryStage); // Update UI components
        });

        primaryStage.setTitle("Localization Example");
        //Create the GridPane layout:
        //Create a GridPane to organize the UI components:
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);
        //Create labels and text fields:
        //Create labels and text fields for first name, last name, and email:
        firstNameLabel = new Label();
        TextField firstNameInput = new TextField();

        lastNameLabel = new Label();
        TextField lastNameInput = new TextField();

        emailLabel = new Label();
        TextField emailInput = new TextField();
        //Create the save button:
        //Create a button for saving data:
        saveButton = new Button();
        //Add language selector to the grid:
        //Add the language selector ComboBox to the grid:
        grid.add(new Label("Select Language: "), 0, 0);
        grid.add(languageSelector, 1, 0);

        //Add UI components to the grid:
        //Add labels, text fields, and the save button to the grid:

        grid.add(firstNameLabel, 0, 1);
        grid.add(firstNameInput, 1, 1);
        grid.add(lastNameLabel, 0, 2);
        grid.add(lastNameInput, 1, 2);
        grid.add(emailLabel, 0, 3);
        grid.add(emailInput, 1, 3);
        grid.add(saveButton, 1, 4);
        //Create the scene and set it to the primary stage:
        //Create a scene with the grid layout and set it to the primary stage:
        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load the default resource bundle:
        //Load the default resource bundle (English) when the application starts:
        bundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
        //Update the UI components based on the resource bundle:
        //Update the UI components with text from the resource bundle:
        updateUI(primaryStage);

        // Save data event handler
        saveButton.setOnAction(e -> saveData(firstNameInput.getText(), lastNameInput.getText(), emailInput.getText(), languageSelector.getValue()));

    }
        //Implement the updateUI method:
        //Implement the updateUI method to update UI components based on the selected language:
    private void updateUI(Stage primaryStage) {
        primaryStage.setTitle(bundle.getString("app.title"));
        // Update labels and button text
        firstNameLabel.setText(bundle.getString("label.firstName"));
        lastNameLabel.setText(bundle.getString("label.lastName"));
        emailLabel.setText(bundle.getString("label.email"));
        saveButton.setText(bundle.getString("button.save"));
    }
        //Implement the saveData method:
        //Implement the saveData method to save data to the appropriate table based on the selected language:
    private void saveData(String firstName, String lastName, String email, String selectedLanguage) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://localhost:3306/fxdemo";
            Connection conn = DriverManager.getConnection(jdbcUrl, "root","Test12");

            // Determine the table name based on the selected language
            String tableName;
            switch(selectedLanguage) {
                case "Farsi":
                    tableName = "employee_fa";
                    break;
                case "Japanese":
                    tableName = "employee_ja";
                    break;
                default:
                    tableName = "employee_en"; // Default to English
            }

            // Insert data into the determined table
            String sql = "INSERT INTO " + tableName + " (first_name, last_name, email) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.executeUpdate();
            System.out.println(bundle.getString("message.saved"));
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
