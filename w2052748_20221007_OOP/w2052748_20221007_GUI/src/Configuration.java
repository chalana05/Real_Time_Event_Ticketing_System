import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class Configuration{
    private static final String URL = "jdbc:mysql://localhost:3306/eventdb";
    private static final String USER = "root";
    private static final String PASSWORD = "CHA@la";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static boolean saveEvent(String totalTickets, String releaseRate, String retrievalRate, String maxCapacity, String customerPurchase) {
        String query = "INSERT INTO events (total_tickets, release_rate, retrieval_rate, max_capacity, customer_purchase) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, totalTickets);
            preparedStatement.setString(2, releaseRate);
            preparedStatement.setString(3, retrievalRate);
            preparedStatement.setString(4, maxCapacity);
            preparedStatement.setString(5, customerPurchase);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String[] loadEvent() {
        String query = "SELECT total_tickets, release_rate, retrieval_rate, max_capacity, customer_purchase FROM events";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return new String[] {
                        resultSet.getString("total_tickets"),
                        resultSet.getString("release_rate"),
                        resultSet.getString("retrieval_rate"),
                        resultSet.getString("max_capacity"),
                        resultSet.getString("customer_purchase")
                };
            } else {
                System.out.println("No event found in the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean clearDatabase() {
        String query = "DELETE FROM events";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean purchaseTickets(int ticketsToPurchase) {
        String queryFetch = "SELECT totalTickets, customerPurchase FROM events WHERE id = (SELECT MAX(id) FROM events)";
        String queryUpdate = "UPDATE events SET totalTickets = ?, customerPurchase = ? WHERE id = (SELECT MAX(id) FROM events)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement fetchStatement = connection.prepareStatement(queryFetch);
             PreparedStatement updateStatement = connection.prepareStatement(queryUpdate);
             ResultSet resultSet = fetchStatement.executeQuery()) {

            if (resultSet.next()) {
                int totalTickets = resultSet.getInt("totalTickets");
                int currentCustomerPurchase = resultSet.getInt("customerPurchase");

                if (ticketsToPurchase > totalTickets) {
                    System.out.println("Not enough tickets available for purchase.");
                    return false;
                }

                // Update total tickets and customer purchase
                int newTotalTickets = totalTickets - ticketsToPurchase;
                int newCustomerPurchase = currentCustomerPurchase + ticketsToPurchase;

                updateStatement.setInt(1, newTotalTickets);
                updateStatement.setInt(2, newCustomerPurchase);

                int rowsAffected = updateStatement.executeUpdate();
                return rowsAffected > 0;
            } else {
                System.out.println("No event found to update.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean updateEvent(String newPurchase) {
        // Your logic to update the event with the new purchase value
        // Return true if successful, false otherwise
        System.out.println("Updating event with new purchase value: " + newPurchase);
        // Example logic (adjust as per your database or logic)
        try {
            int purchaseValue = Integer.parseInt(newPurchase);
            // Update your data source with the new value here
            return true; // Indicate success
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format: " + newPurchase);
            return false; // Indicate failure
        }
    }
}


