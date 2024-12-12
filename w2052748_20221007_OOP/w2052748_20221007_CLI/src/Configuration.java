import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;


class Configuration {
    private int totalTickets;
    private int maxTicketCapacity;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int customerPurchased;

    // Constructor
    public Configuration(int totalTickets, int maxTicketCapacity, int ticketReleaseRate, int customerRetrievalRate, int customerPurchased) {
        this.totalTickets = totalTickets;
        this.maxTicketCapacity = maxTicketCapacity;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.customerPurchased = customerPurchased;
    }

    // Getters and Setters
    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getCustomerPurchased() {
        return customerPurchased;
    }

    public void setCustomerPurchased(int customerPurchased) {
        this.customerPurchased = customerPurchased;
    }

    // Save configuration to a JSON file
    public void saveToJson(String filePath) {
        Gson gson = new Gson(); // Create a Gson object
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(this, writer); // Convert this object to JSON and save it to the file
            System.out.println("Configuration saved to " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving configuration to JSON file.");
            e.printStackTrace();
        }
    }

    // Load configuration from a JSON file
    public static Configuration loadFromJson(String filePath) {
        Gson gson = new Gson(); // Create a Gson object
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, Configuration.class); // Deserialize the JSON into a Configuration object
        } catch (IOException e) {
            System.out.println("Error loading configuration from JSON file.");
            e.printStackTrace();
            return null; // Return null if an error occurs
        }
    }

    // Save configuration to a plain text file
    public void saveToTextFile(String filePath) {
        try {
            // Create the parent directories if they do not exist
            File file = new File(filePath);
            file.getParentFile().mkdirs();

            // Now save the configuration
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("Total Tickets: " + totalTickets + "\n");
                writer.write("Max Ticket Capacity: " + maxTicketCapacity + "\n");
                writer.write("Ticket Release Rate: " + ticketReleaseRate + "\n");
                writer.write("Customer Retrieval Rate: " + customerRetrievalRate + "\n");
                writer.write("Customer Purchased: " + customerPurchased + "\n");
                System.out.println("Configuration saved to " + filePath);
            }
        } catch (IOException e) {
            System.out.println("Error saving configuration to text file.");
            e.printStackTrace();
        }
    }

    // Main method to test saving and loading configurations
    public static void main(String[] args) {
        // Create a sample Configuration object
        Configuration config = new Configuration(1000, 500, 10, 5, 250);

        // Save the configuration to a plain text file
        config.saveToTextFile("ticket_system_config.txt");

        // Save the configuration to a JSON file
        config.saveToJson("ticket_system_config.json");

        // Load the configuration from a JSON file
        Configuration loadedConfig = Configuration.loadFromJson("ticket_system_config.json");
        if (loadedConfig != null) {
            System.out.println("Loaded configuration from JSON:");
            System.out.println("Total Tickets: " + loadedConfig.getTotalTickets());
            System.out.println("Max Ticket Capacity: " + loadedConfig.getMaxTicketCapacity());
            System.out.println("Ticket Release Rate: " + loadedConfig.getTicketReleaseRate());
            System.out.println("Customer Retrieval Rate: " + loadedConfig.getCustomerRetrievalRate());
            System.out.println("Customer Purchased: " + loadedConfig.getCustomerPurchased());
        }
    }
}
