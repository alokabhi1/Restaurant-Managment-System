package BU_PROJECT;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashSet;

// Abstract User class
abstract class User {
    protected String userID;
    protected String username;
    protected String password;

    private static final HashSet<String> usernames = new HashSet<>();
    private static final HashSet<String> userIDs = new HashSet<>();

    public User(String username, String password) {
        if (usernames.contains(username)) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken. Please choose a different one.");
        }
        this.username = username;
        this.password = password;
        this.userID = generateUserID();

        usernames.add(this.username);
        userIDs.add(this.userID);
    }

    protected abstract String generateUserID();

    public abstract void login(String username, String password);

    public void logout() {
        System.out.println("Logged out successfully.");
    }

    public void viewProfile() {
        System.out.println("Username: " + username);
    }

    public static void removeUser(String username) {
        usernames.remove(username);
    }
}

// Admin class
class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
    }

    @Override
    protected String generateUserID() {
        return "ADM" + (int) (Math.random() * 1000);
    }

    @Override
    public void login(String username, String password) {
        if (this.username.equals(username) && this.password.equals(password)) {
            System.out.println("Admin logged in successfully.");
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    public void editProfile(Scanner scanner) {
        System.out.print("Enter new username: ");
        String newUsername = scanner.nextLine();
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        removeUser(this.username);
        this.username = newUsername;
        this.password = newPassword;
        System.out.println("Profile updated successfully.");
    }

    public void addMenuItem(ArrayList<MenuItem> menu, Scanner scanner) {
        System.out.print("Enter item ID: ");
        String itemID = scanner.nextLine();
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        System.out.print("Enter item description: ");
        String description = scanner.nextLine();
        System.out.print("Enter item price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        menu.add(new MenuItem(itemID, name, description, price));
        System.out.println("Menu item added successfully.");
    }

    public void removeMenuItem(ArrayList<MenuItem> menu, Scanner scanner) {
        System.out.print("Enter item ID to remove: ");
        String itemID = scanner.nextLine();
        MenuItem itemToRemove = null;
        for (MenuItem item : menu) {
            if (item.getItemID().equals(itemID)) {
                itemToRemove = item;
                break;
            }
        }
        if (itemToRemove != null) {
            menu.remove(itemToRemove);
            System.out.println("Menu item removed successfully.");
        } else {
            System.out.println("Menu item not found.");
        }
    }

    public void viewAllOrders(ArrayList<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("No orders placed yet.");
        } else {
            for (Order order : orders) {
                System.out.println(order);
            }
        }
    }
}

// Customer class
class Customer extends User {
    private ArrayList<Order> orderHistory = new ArrayList<>();

    public Customer(String username, String password) {
        super(username, password);
    }

    @Override
    protected String generateUserID() {
        return "CUST" + (int) (Math.random() * 1000);
    }

    @Override
    public void login(String username, String password) {
        if (this.username.equals(username) && this.password.equals(password)) {
            System.out.println("Customer logged in successfully.");
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    public void viewMenu(ArrayList<MenuItem> menu) {
        System.out.println("\n--- Menu ---");
        for (MenuItem item : menu) {
            item.displayItem();
        }
    }

    public void viewOrderHistory() {
        if (orderHistory.isEmpty()) {
            System.out.println("No orders in your history.");
        } else {
            for (Order order : orderHistory) {
                System.out.println(order);
            }
        }
    }

    public void placeOrder(Order order) {
        orderHistory.add(order);
        System.out.println("Order placed successfully!");
    }
}

// MenuItem class
class MenuItem {
    private String itemID;
    private String name;
    private String description;
    private double price;

    public MenuItem(String itemID, String name, String description, double price) {
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getItemID() {
        return itemID;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void displayItem() {
        System.out.println("ID: " + itemID + ", Name: " + name + ", Description: " + description + ", Price: " + price);
    }
}

// Order class
class Order {
    private String orderID;
    private ArrayList<OrderItem> items = new ArrayList<>();
    private String orderType;

    public Order(String orderID, Customer customer) {
        this.orderID = orderID;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void setOrderType(String type) {
        this.orderType = type;
    }

    public void generateReceipt() {
        System.out.println("\n--- Receipt ---");
        double total = 0;
        for (OrderItem item : items) {
            total += item.getTotalPrice();
            System.out.println(item);
        }
        System.out.println("Total: $" + total);
    }

    @Override
    public String toString() {
        return "Order ID: " + orderID + ", Items: " + items + ", Type: " + orderType;
    }
}

// OrderItem class
class OrderItem {
    private MenuItem item;
    private int quantity;

    public OrderItem(MenuItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return item.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return item.getName() + " x " + quantity + " - $" + getTotalPrice();
    }
}

// Main Class
public class RESTAURANT_MANAGMENT_SYSTEM{
    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<Order> orders = new ArrayList<>();
    static ArrayList<MenuItem> menu = new ArrayList<>();
    static User currentUser = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Adding some sample menu items
        menu.add(new MenuItem("001", "Pizza", "Cheese Pizza", 8.99));
        menu.add(new MenuItem("002", "Burger", "Mutton Burger", 5.99));
        menu.add(new MenuItem("003", "Pasta", "Spaghetti Pasta", 7.49));

        // Adding admin users
        users.add(new Admin("alok", "alok123"));
        users.add(new Admin("akshul", "akshul123"));
        users.add(new Admin("rahul", "rahul123"));
        

        while (true) {
            System.out.println("\nWelcome to the Restaurant Ordering System!");
            System.out.println("1. Register as a Customer");
            System.out.println("2. Login as a Customer");
            System.out.println("3. Login as Admin");
            System.out.println("4. Exit");
            System.out.print("Please enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerUser(scanner);
                    break;
                case 2:
                    loginUser(scanner);
                    break;
                case 3:
                    loginAdmin(scanner);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
            }
        }
    }

    private static void registerUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        try {
            Customer newCustomer = new Customer(username, password);
            users.add(newCustomer);
            System.out.println("Customer registered successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void loginUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        for (User user : users) {
            if (user.username.equals(username) && user instanceof Customer) {
                user.login(username, password);
                currentUser = user;
                customerMenu(scanner);  // Move to customer menu
                return;
            }
        }
        System.out.println("User not found.");
    }

    private static void loginAdmin(Scanner scanner) {
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user instanceof Admin && user.username.equals(username)) {
                user.login(username, password);
                currentUser = user;
                adminMenu(scanner, (Admin) user);  // Move to admin menu
                return;
            }
        }
        System.out.println("Invalid admin credentials or admin not found.");
    }

    private static void adminMenu(Scanner scanner, Admin admin) {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. View Profile");
            System.out.println("2. Edit Profile");
            System.out.println("3. Add Menu Item");
            System.out.println("4. Remove Menu Item");
            System.out.println("5. View All Orders");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    admin.viewProfile();
                    break;
                case 2:
                    admin.editProfile(scanner);
                    break;
                case 3:
                    admin.addMenuItem(menu, scanner);
                    break;
                case 4:
                    admin.removeMenuItem(menu, scanner);
                    break;
                case 5:
                    admin.viewAllOrders(orders);
                    break;
                case 6:
                    admin.logout();
                    currentUser = null;
                    return;
            }
        }
    }

    private static void customerMenu(Scanner scanner) {
        if (currentUser == null) return;

        Order currentOrder = new Order("ORD" + (orders.size() + 1), (Customer) currentUser);

        while (true) {
            System.out.println("\nWelcome " + currentUser.username);
            System.out.println("1. View Menu");
            System.out.println("2. View Order History");
            System.out.println("3. Complete Order");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                // Show the menu to the customer
                ((Customer) currentUser).viewMenu(menu);
                placeOrder(scanner, currentOrder);  // Skip welcome and go directly to menu
            } else if (choice == 2) {
                ((Customer) currentUser).viewOrderHistory();
            } else if (choice == 3) {
                completeOrder(scanner, currentOrder);
                break;
            } else if (choice == 4) {
                currentUser.logout();
                currentUser = null;
                break;
            }
        }
    }

    private static void placeOrder(Scanner scanner, Order currentOrder) {
        System.out.print("Enter the item ID to order: ");
        String itemID = scanner.nextLine();
        MenuItem selectedItem = null;

        for (MenuItem item : menu) {
            if (item.getItemID().equals(itemID)) {
                selectedItem = item;
                break;
            }
        }

        if (selectedItem != null) {
            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            currentOrder.addItem(new OrderItem(selectedItem, quantity));
            System.out.println("Your item has been added to the order.");
            System.out.print("Would you like to add another item? (y/n): ");
            String anotherItem = scanner.nextLine();
            if (anotherItem.equalsIgnoreCase("n")) {
                completeOrder(scanner, currentOrder);
            } else {
                placeOrder(scanner, currentOrder);  // Directly return to order process
            }
        } else {
            System.out.println("Invalid item ID. Please choose a valid item ID.");
        }
    }

    private static void completeOrder(Scanner scanner, Order currentOrder) {
        System.out.println("Please select Order Type:");
        System.out.println("1. Dine-In");
        System.out.println("2. Takeaway");
        int orderTypeChoice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        if (orderTypeChoice == 1) {
            currentOrder.setOrderType("Dine-In");
        } else if (orderTypeChoice == 2) {
            currentOrder.setOrderType("Takeaway");
        } else {
            System.out.println("Invalid choice. Defaulting to Dine-In.");
            currentOrder.setOrderType("Dine-In");
        }

        currentOrder.generateReceipt();

        // Payment options
        System.out.print("Enter payment method (1 for Cash, 2 for Card): ");
        int paymentMethod = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (paymentMethod == 1) {
            System.out.println("Paid by Cash.");
        } else if (paymentMethod == 2) {
            System.out.println("Paid by Card.");
        } else {
            System.out.println("Invalid payment method.");
        }

        System.out.println("Thank you for your order!");
        ((Customer) currentUser).placeOrder(currentOrder);
        orders.add(currentOrder);
    }
}