import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {
    private TicketPool ticketPool;
    private VBox outputBox;
    private ExecutorService executorService;

    @Override
    public void start(Stage primaryStage) {
        showMainMenu(primaryStage);
    }

    private void showMainMenu(Stage primaryStage) {
        // Create the initial label
        Text label = new Text("Real-time Event Ticketing System");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Create the start button
        Button startButton = new Button("Start the system");
        startButton.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-font-size: 14px;");

        // Create the VBox layout
        VBox vbox = new VBox(20); // 20px spacing
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: linear-gradient(to bottom right, #e8f0ff, #ffffff);");
        vbox.getChildren().addAll(label, startButton);

        // Event handler for the start button
        startButton.setOnAction(e -> {
            // Clear the VBox and add new options
            vbox.getChildren().clear();

            // Add "Create New Event" text
            Text createEventText = new Text("Create New Event");
            createEventText.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            // Add "Create" and "Load" buttons
            Button createButton = new Button("Start New Event");
            createButton.setStyle("-fx-background-color: LIME; -fx-text-fill: black; -fx-pref-width: 150px;");
            Button loadButton = new Button("Load Configuration");
            loadButton.setStyle("-fx-background-color: LIME; -fx-text-fill: black; -fx-pref-width: 150px;");


            Button exitButton = new Button("Exit");
            exitButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-pref-width: 150px;");
            exitButton.setOnAction(event -> {
                primaryStage.close();
            });
            // Set actions for the buttons
            createButton.setOnAction(event -> showCreateEventForm(primaryStage));
            loadButton.setOnAction(event -> showLoadEventForm(primaryStage));

            // Update the VBox with the new text and buttons
            vbox.getChildren().addAll(createEventText, createButton, loadButton,exitButton);
        });

        // Create the Scene and set it on the Stage
        Scene scene = new Scene(vbox, 450, 350);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Real-time Event Ticketing System");
        primaryStage.show();
    }

    private void showTicketPool(Stage primaryStage, String eventDetails) {
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #f2f2f2);");

        // Header Text
        Text title = new Text("Ticket Pool");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        title.setFill(Color.web("#333333"));

        // Card-style content for details
        VBox card = new VBox(15);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; "
                + "-fx-border-color: #cccccc; "
                + "-fx-border-radius: 10; "
                + "-fx-background-radius: 10; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.5, 0, 0);");

        Text details = new Text(eventDetails);
        details.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        details.setFill(Color.web("#555555"));
        details.setWrappingWidth(400);
        card.getChildren().add(details);

        // Output area
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);

        // TicketPool creation
        int totalTickets = Integer.parseInt(eventDetails.split("\n")[0].split(": ")[1]);
        int releaseRate = Integer.parseInt(eventDetails.split("\n")[1].split(": ")[1]);
        int retrievalRate = Integer.parseInt(eventDetails.split("\n")[2].split(": ")[1]);
        int maxCapacity = Integer.parseInt(eventDetails.split("\n")[3].split(": ")[1]);
        int customerPurchase = Integer.parseInt(eventDetails.split("\n")[4].split(": ")[1]);

        TicketPool ticketPool = new TicketPool(totalTickets, maxCapacity, outputArea);

        // Create vendors and customers with parameters
        Vendor vendor1 = new Vendor(ticketPool, releaseRate, 1);
        Customer customer1 = new Customer(ticketPool, retrievalRate, customerPurchase, 1);

        // ExecutorService to manage threads
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(vendor1);
        executorService.execute(customer1);

        // Add a shutdown hook for proper cleanup
        primaryStage.setOnCloseRequest(event -> executorService.shutdownNow());

        // Back Button
        Button backButton = new Button("Back to Menu");
        backButton.setStyle("-fx-background-color: DARKGREY; -fx-text-fill: black; -fx-pref-width: 100px;");
        backButton.setOnAction(event -> {
            executorService.shutdownNow();
            VBox vbox = createEventOptions(primaryStage);
            Scene optionsScene = new Scene(vbox, 450, 350);
            primaryStage.setScene(optionsScene);
        });

        // Add all components to the main layout
        mainLayout.getChildren().addAll(title, card, outputArea, backButton);

        // Create and set the scene
        Scene scene = new Scene(mainLayout, 500, 400);
        primaryStage.setScene(scene);
    }



    private void showCreateEventForm(Stage primaryStage) {
        if (Configuration.clearDatabase()) {
            System.out.println("Database cleared successfully.");
        } else {
            System.out.println("Failed to clear database.");
        }

        Configuration.loadEvent(); // Deletes all records from the database

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label titleLabel = new Label("Starting a New Event");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gridPane.add(titleLabel, 0, 0, 2, 1);

        Label subTitleLabel = new Label("Enter event details here");
        subTitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        gridPane.add(subTitleLabel, 0, 1, 2, 1);

        String[] labels = {
                "Enter total number of tickets",
                "Enter ticket release rate",
                "Enter customer ticket retrieval rate",
                "Enter maximum ticket capacity",
                "Enter number of tickets customer purchase"
        };

        TextField[] fields = new TextField[labels.length];
        Text errorMessage = new Text();
        errorMessage.setFill(Color.RED);

        int rowIndex = 2;
        for (int i = 0; i < labels.length; i++) {
            Label fieldLabel = new Label(labels[i]);
            TextField textField = new TextField();
            fields[i] = textField;

            gridPane.add(fieldLabel, 0, rowIndex);
            gridPane.add(textField, 1, rowIndex);
            rowIndex++;
        }

        gridPane.add(errorMessage, 0, rowIndex);

        Button startButton = new Button("Start");
        startButton.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-pref-width: 100px;");
        startButton.setOnAction(event -> {
            boolean allValid = true;

            errorMessage.setText("");
            for (TextField textField : fields) {
                textField.setStyle("");
            }

            for (TextField textField : fields) {
                if (!textField.getText().matches("\\d*")) {
                    allValid = false;
                    textField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                }
            }

            if (allValid) {
                int totalTickets = Integer.parseInt(fields[0].getText());
                int maxCapacity = Integer.parseInt(fields[3].getText());

                if (maxCapacity >= totalTickets) {
                    allValid = false;
                    fields[3].setStyle("-fx-border-color: red; -fx-border-width: 2;");
                    errorMessage.setText("Maximum capacity must be less than the total number of tickets.");
                }
            }

            if (!allValid) {
                if (errorMessage.getText().isEmpty()) {
                    errorMessage.setText("Please enter valid numbers in all fields.");
                }
            } else {
                boolean saved = Configuration.saveEvent(
                        fields[0].getText(),
                        fields[1].getText(),
                        fields[2].getText(),
                        fields[3].getText(),
                        fields[4].getText()
                );

                if (saved) {
                    String eventDetails = String.format(
                            "Total Tickets: %s\nRelease Rate: %s\nRetrieval Rate: %s\nMax Capacity: %s\nCustomer Purchase: %s",
                            fields[0].getText(), fields[1].getText(), fields[2].getText(),
                            fields[3].getText(), fields[4].getText()
                    );
                    showTicketPool(primaryStage, eventDetails);
                }
            }
        });

        Button backButton = new Button("Back to Menu");
        backButton.setStyle("-fx-background-color: DARKGREY; -fx-text-fill: black; -fx-pref-width: 100px;");

        backButton.setOnAction(event -> {
            VBox vbox = createEventOptions(primaryStage);
            Scene optionsScene = new Scene(vbox, 450, 350);
            primaryStage.setScene(optionsScene);
        });

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(backButton, startButton);
        gridPane.add(buttonBox, 0, rowIndex + 1, 2, 1);

        Scene formScene = new Scene(gridPane, 450, 350);
        primaryStage.setScene(formScene);
    }

    private void showLoadEventForm(Stage primaryStage) {
        // Create a GridPane for the load form layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Add title labels
        Label titleLabel = new Label("Load Existing Event");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gridPane.add(titleLabel, 0, 0, 2, 1); // Span 2 columns

        // Add "Enter number of tickets customer purchase" input field
        gridPane.add(new Label("Enter number of tickets to purchase:"), 0, 1);
        TextField textField = new TextField();
        gridPane.add(textField, 1, 1);

        // Add validation listener
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Allow only digits
                textField.setText(newValue.replaceAll("[^\\d]", "")); // Remove non-digit characters
                textField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            } else {
                textField.setStyle("-fx-border-color: green; -fx-border-width: 2;");
            }
        });

        // Section to display loaded event details
        Label eventDetailsLabel = new Label("Event Details:");
        eventDetailsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        gridPane.add(eventDetailsLabel, 0, 3, 2, 1); // Span 2 columns

        // Placeholder for event details
        Text eventDetailsText = new Text();
        eventDetailsText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        eventDetailsText.setWrappingWidth(400);
        gridPane.add(eventDetailsText, 0, 4, 2, 1); // Span 2 columns

        // Add "Back", "Load Event", "Update Customer Purchase", and "Purchase Tickets" buttons
        Button backButton = new Button("Back to Menu");
        backButton.setStyle("-fx-background-color: DARKGREY; -fx-text-fill: black; -fx-pref-width: 100px;");
        backButton.setOnAction(event -> {
            VBox vbox = createEventOptions(primaryStage);
            Scene optionsScene = new Scene(vbox, 450, 350);
            primaryStage.setScene(optionsScene);
        });

        Button loadButton = new Button("View Event");
        loadButton.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-pref-width: 100px;");

        loadButton.setOnAction(event -> {
            String[] eventDetails = Configuration.loadEvent();
            if (eventDetails != null) {
                String eventDetailsDisplay = String.format(
                        "Total Tickets: %s\nRelease Rate: %s\nRetrieval Rate: %s\nMax Capacity: %s\nCustomer Purchase: %s",
                        eventDetails[0], eventDetails[1], eventDetails[2], eventDetails[3], eventDetails[4]
                );
                eventDetailsText.setText(eventDetailsDisplay);
            } else {
                eventDetailsText.setText("No existing event found.");
            }
        });

        Button updateButton = new Button("Add Ticket");
        updateButton.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-pref-width: 100px;");
        updateButton.setOnAction(event -> {
            String newPurchase = textField.getText();
            if (newPurchase.isEmpty() || !newPurchase.matches("\\d+")) {
                eventDetailsText.setText("Please enter a valid number for customer purchase.");
                return;
            }

            if (Configuration.updateEvent(newPurchase)) {
                eventDetailsText.setText("Customer purchase updated successfully.\nReloading event details...");
                loadButton.fire(); // Reload event details to reflect the updated purchase
            } else {
                eventDetailsText.setText("Error updating customer purchase.");
            }
        });


        // Add buttons to the layout
        HBox buttonBox = new HBox(10); // 10px spacing between buttons
        buttonBox.getChildren().addAll(backButton, loadButton, updateButton);
        gridPane.add(buttonBox, 0, 5, 2, 1); // Span across two columns

        // Create a new Scene for the load form and set it on the stage
        Scene formScene = new Scene(gridPane, 450, 400); // Increased height to accommodate event details
        primaryStage.setScene(formScene);
    }

    private VBox createEventOptions(Stage primaryStage) {
        // Create a VBox for the event options layout
        VBox vbox = new VBox(20); // 20px spacing
        vbox.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: linear-gradient(to bottom right, #e8f0ff, #ffffff);");

        // Add title
        Text title = new Text("Event Options");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        vbox.getChildren().add(title);

        // Add "Create" and "Load" buttons
        Button createButton = new Button("Start New Event");
        createButton.setStyle("-fx-background-color: LIME; -fx-text-fill: black; -fx-pref-width: 150px;");
        Button loadButton = new Button("Load Configuration");
        loadButton.setStyle("-fx-background-color: LIME; -fx-text-fill: black; -fx-pref-width: 150px;");


        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-pref-width: 150px;");
        exitButton.setOnAction(event -> {
            primaryStage.close();
        });
        // Set actions for the buttons
        createButton.setOnAction(event -> showCreateEventForm(primaryStage));
        loadButton.setOnAction(event -> showLoadEventForm(primaryStage));

        // Update the VBox with the new text and buttons
        vbox.getChildren().addAll(createButton, loadButton,exitButton);

        return vbox;
    }
    public static void main(String[] args) {
        launch(args);
    }
}


