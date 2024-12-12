import javafx.scene.control.TextArea;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int customerRetrievalRate;
    private final int ticketsToPurchase;
    private final int customerId; // Customer ID

    public Customer(TicketPool ticketPool, int customerRetrievalRate, int ticketsToPurchase, int customerId) {
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
        this.ticketsToPurchase = ticketsToPurchase;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        int ticketsPurchased = 0;
        while (ticketsPurchased < ticketsToPurchase) {
            synchronized (ticketPool) {
                if (ticketPool.retrieveTicket()) {
                    ticketsPurchased++;
                    ticketPool.updateOutput("Customer " + customerId + " purchased a ticket successfully.");
                }
            }
            try {
                Thread.sleep(customerRetrievalRate);  // Wait for the customer retrieval rate
            } catch (InterruptedException e) {
                ticketPool.updateOutput("Customer " + customerId + " thread interrupted.");
                break;
            }
        }
    }
}
