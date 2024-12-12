# Real-Time Event Ticketing System

This project is a **Real-Time Event Ticketing System** implemented in Java. It simulates a ticketing system where vendors release tickets and customers purchase them in real-time. The system allows for multi-threaded execution, ensuring vendors and customers operate concurrently.

---

## Features

- **Start a New Event:** Configure and save a new event with details such as total tickets, ticket release rate, customer retrieval rate, and maximum ticket capacity.
- **Load Existing Configuration:** Load previously saved configurations from a JSON file.
- **Concurrency:** Vendors release tickets and customers purchase tickets simultaneously using Java threads.
- **Persistence:** Save and load configurations in JSON format for reuse.
- **User-Friendly CLI:** Command-line interface to interact with the system.

---

## Getting Started

### Prerequisites
- **Java JDK** (Version 8 or above)
- **Gson Library** (for JSON handling)

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/real-time-ticketing.git
   cd real-time-ticketing
   ```

2. Add the Gson library to your project:
   - Download Gson from [Maven Repository](https://mvnrepository.com/artifact/com.google.code.gson/gson).
   - Include the `.jar` file in your project's classpath.

3. Compile the code:
   ```bash
   javac -cp .:gson-<version>.jar Main.java Configuration.java TicketPool.java Vendor.java Customer.java
   ```

4. Run the program:
   ```bash
   java -cp .:gson-<version>.jar Main
   ```

---

## Usage

1. **Run the Program:** Execute the program and follow the prompts on the CLI.

2. **Options:**
   - Select **1** to start a new event and configure ticketing details.
   - Select **2** to load an existing configuration from `config.json`.
   - Select **3** to exit the program.

3. **Start Ticketing System:** After configuration, the ticketing system will run with vendors adding tickets and customers purchasing them.

---

## Code Overview

### Main.java
- Entry point of the application.
- Handles user interaction via CLI.

### Configuration.java
- Manages event configurations.
- Provides methods to save/load configurations in JSON format.

### TicketPool.java
- Centralized ticket management system.
- Ensures thread-safe operations for adding and retrieving tickets.

### Vendor.java
- Simulates ticket release by a vendor.
- Operates on `TicketPool` using a specified release rate.

### Customer.java
- Simulates ticket purchase by a customer.
- Operates on `TicketPool` using a specified retrieval rate.

---

## Sample Output
```plaintext
========== Real Time Event Ticketing System ==========
1. Start a New Event
2. Load Existing Configuration
3. Exit Program
======================================================
Enter your choice (1, 2, or 3): 1

        --- Configure New Event ---

Enter Total Number of Tickets        : 100
Enter Ticket Release Rate (in ms)    : 100
Enter Customer Retrieval Rate (in ms): 150
Enter Maximum Ticket Capacity        : 50
Enter Number of Tickets to Purchase  : 10
Configuration saved successfully!

========== Starting Ticket System ==========
Vendor 1 added a ticket.
Vendor 2 added a ticket.
Customer 1 purchased a ticket successfully.
Customer 2 purchased a ticket successfully.
...
```

---

## Contributing

Contributions are welcome! Feel free to open an issue or submit a pull request.

---

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.

---

## Acknowledgments

- [Gson Library](https://github.com/google/gson) for JSON handling.
- Java's built-in threading mechanisms for concurrency.

---

