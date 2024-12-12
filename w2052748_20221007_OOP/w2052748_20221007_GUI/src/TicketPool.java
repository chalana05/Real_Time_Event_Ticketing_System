import javafx.scene.control.TextArea;

public class TicketPool {
    private int totalTickets;
    private int currentCapacity;
    private final int maxCapacity;
    private TextArea outputArea;

    public TicketPool(int totalTickets, int maxCapacity, TextArea outputArea) {
        this.totalTickets = totalTickets;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0; // initially no tickets are available
        this.outputArea = outputArea;
    }

    // Check if the vendor can add more tickets
    public synchronized boolean canAddMoreTickets() {
        return currentCapacity < maxCapacity;
    }

    // Add a ticket to the pool
    public synchronized void addTicket() {
        if (canAddMoreTickets()) {
            currentCapacity++;
            updateOutput("Vendor added Ticket successfully.");
        }
    }

    // Retrieve a ticket from the pool
    public synchronized boolean retrieveTicket() {
        if (currentCapacity > 0) {
            currentCapacity--;
            return true;
        }
        return false;
    }

    // Get the current number of tickets
    public synchronized int getCurrentTickets() {
        return currentCapacity;
    }

    // Getter for maxCapacity
    public int getMaxCapacity() {
        return maxCapacity;
    }

    // Update the TextArea with the given message
    public void updateOutput(String message) { // Changed to public
        outputArea.appendText(message + "\n");
    }
}
