import javafx.scene.control.TextArea;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketReleaseRate;
    private final int vendorId; // Vendor ID

    public Vendor(TicketPool ticketPool, int ticketReleaseRate, int vendorId) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.vendorId = vendorId;
    }

    @Override
    public void run() {
        while (ticketPool.getCurrentTickets() < ticketPool.getMaxCapacity()) {
            synchronized (ticketPool) {
                if (ticketPool.canAddMoreTickets()) {
                    ticketPool.addTicket(); // Add a ticket if there is space
                    ticketPool.updateOutput("Vendor " + vendorId + " added a ticket.");
                }
            }
            try {
                Thread.sleep(ticketReleaseRate);  // Wait for the ticket release rate
            } catch (InterruptedException e) {
                ticketPool.updateOutput("Vendor " + vendorId + " thread interrupted.");
                break;
            }
        }
    }
}
