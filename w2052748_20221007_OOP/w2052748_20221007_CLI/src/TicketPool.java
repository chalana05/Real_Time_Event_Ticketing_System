public class TicketPool {
    private int totalTickets;
    private int currentCapacity;
    private final int maxCapacity;

    public TicketPool(int totalTickets, int maxCapacity) {
        this.totalTickets = totalTickets;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0; // initially no tickets are available
    }

    // Check if the vendor can add more tickets
    public synchronized boolean canAddMoreTickets() {
        return currentCapacity < maxCapacity;
    }

    // Add a ticket to the pool
    public synchronized void addTicket() {
        if (canAddMoreTickets()) {
            currentCapacity++;
//            System.out.println("Vendor added Ticket successfully.");
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
}
