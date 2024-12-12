import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Configuration currentConfig = null;

        while (true) { // Keep the program running until the user decides to exit
            System.out.println("\n========== Real Time Event Ticketing System ==========");
            System.out.println("1. Start a New Event");
            System.out.println("2. Load Existing Configuration");
            System.out.println("3. Exit Program");
            System.out.println("======================================================");
            int choice = getValidPositiveIntegerInput(scanner, "\nEnter your choice (1, 2, or 3): ");

            if (choice == 1) {
                System.out.println("\n        --- Configure New Event ---\n");

                int totalTickets = getValidPositiveIntegerInput(scanner, "Enter Total Number of Tickets        : ");
                int ticketReleaseRate = getValidPositiveIntegerInput(scanner, "Enter Ticket Release Rate (in ms)    : ");
                int customerRetrievalRate = getValidPositiveIntegerInput(scanner, "Enter Customer Retrieval Rate (in ms): ");
                int maxTicketCapacity = getValidMaxTicketCapacity(scanner, totalTickets);
                int ticketsToPurchase = getValidPositiveIntegerInput(scanner, "Enter Number of Tickets to Purchase  : ");

                currentConfig = new Configuration(totalTickets, maxTicketCapacity, ticketReleaseRate, customerRetrievalRate, ticketsToPurchase);

                currentConfig.saveToJson("config.json");
                System.out.println("Configuration saved successfully!");
                break; // Exit loop and proceed to ticketing system
            } else if (choice == 2) {
                System.out.println("\n      --- Loading Configuration ---\n");

                File configFile = new File("config.json");
                if (!configFile.exists()) {
                    System.out.println("Please create a new event first.");
                    continue; // Restart the loop
                }

                currentConfig = Configuration.loadFromJson("config.json");
                if (currentConfig == null) {
                    System.out.println("Please create a new event first.");
                    continue; // Restart the loop
                }

                // Prompt the user for the number of tickets to purchase
                int ticketsToPurchase = getValidPositiveIntegerInput(scanner, "Enter Number of Tickets to Purchase  : ");
                currentConfig.setCustomerPurchased(ticketsToPurchase);
                break; // Exit loop and proceed to ticketing system
            } else if (choice == 3) {
                System.out.println("\nExiting program. Goodbye!");
                return; // Exit the program
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }


        // Proceed with the ticketing system
        System.out.println("\n========== Starting Ticket System ==========\n");

        TicketPool ticketPool = new TicketPool(currentConfig.getTotalTickets(), currentConfig.getMaxTicketCapacity());

        Vendor vendor = new Vendor(ticketPool, currentConfig.getTicketReleaseRate(), 1); // Vendor ID 1
        Thread vendorThread = new Thread(vendor, "Vendor 1 = ");
        vendorThread.start();

        Customer customer = new Customer(ticketPool, currentConfig.getCustomerRetrievalRate(), currentConfig.getCustomerPurchased(), 01); // Customer ID 101
        Thread customerThread = new Thread(customer, "Customer 1 = ");
        customerThread.start();

        Vendor vendor1 = new Vendor(ticketPool, currentConfig.getTicketReleaseRate(), 2); // Vendor ID 2
        Thread vendor1Thread = new Thread(vendor1, "Vendor 2 = ");
        vendor1Thread.start();

        Customer customer1 = new Customer(ticketPool, currentConfig.getCustomerRetrievalRate(), currentConfig.getCustomerPurchased(), 02); // Customer ID 102
        Thread customer1Thread = new Thread(customer1, "Customer 2 = ");
        customer1Thread.start();


    }

    private static int getValidPositiveIntegerInput(Scanner scanner, String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Integer.parseInt(scanner.nextLine().trim());
                if (value > 0) {
                    return value;
                } else {
                    System.out.println("Invalid input. Please enter a positive integer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private static int getValidMaxTicketCapacity(Scanner scanner, int totalTickets) {
        int value;
        while (true) {
            System.out.print("Enter Maximum Ticket Capacity        : ");
            try {
                value = Integer.parseInt(scanner.nextLine().trim());
                if (value > 0 && value <= totalTickets) {
                    return value;
                } else {
                    System.out.println("Invalid input. Maximum Ticket Capacity must be greater than 0 and less than or equal to Total Tickets.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
}