package Product_Inventory_Management;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Create a list to hold Product objects
        ArrayList<Product> listProduct = new ArrayList<>();
        // Add sample products to the list
        Product product1 = new Product("Laptop", 999.99, 5);
        Product product2 = new Product("Smartphone", 499.99, 10);
        Product product3 = new Product("Tablet", 299.99, 0);
        Product product4 = new Product("Smartwatch", 199.99, 3);
        listProduct.add(product1);
        listProduct.add(product2);
        listProduct.add(product3);
        listProduct.add(product4);

        // Create a Scanner object for user input
        Scanner scanner = new Scanner(System.in);
        boolean continueMenu = true; // Flag to control the menu loop

        // Menu loop
        while (continueMenu) {
            System.out.println("Select an option:");
            System.out.println("1. Calculate Total Inventory Value");
            System.out.println("2. Find the Most Expensive Product");
            System.out.println("3. Check if a Product is in Stock");
            System.out.println("4. Sort Products by Price or Quantity");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            switch (choice) {
                case 1:
                    // Calculate and display the total inventory value
                    double totalValue = calculateTotalInventoryValue(listProduct);
                    System.out.println("Total Inventory Value: $" + totalValue);
                    break;
                case 2:
                    // Find and display the most expensive product
                    String mostExpensive = findTheMostExpensiveProduct(listProduct);
                    System.out.println("Most Expensive Product: " + mostExpensive);
                    break;
                case 3:
                    // Check if a specified product is in stock
                    System.out.print("Enter product name to check: ");
                    String productName = scanner.nextLine();
                    boolean isInStock = checkNameProductInStock(listProduct, productName);
                    System.out.println("Is product in stock? " + isInStock);
                    break;
                case 4:
                    // Sort products by price or quantity and display them
                    System.out.print("Sort by (price/quantity): ");
                    String key = scanner.nextLine();
                    System.out.print("Order (ASC/DESC): ");
                    String order = scanner.nextLine();
                    ArrayList<Product> sortedProducts = sortProductList(listProduct, key, order);
                    System.out.println("Sorted Products:");
                    for (Product product : sortedProducts) {
                        System.out.println(product);
                    }
                    break;
                case 5:
                    // Exit the program
                    continueMenu = false;
                    System.out.println("Exiting the program.");
                    break;
                default:
                    // Handle invalid menu choices
                    System.out.println("Invalid choice! Please try again.");
            }
            System.out.println(); // Print a newline for better readability
        }

        // Close the scanner resource
        scanner.close();
    }

    // Calculate total inventory value
    public static double calculateTotalInventoryValue(ArrayList<Product> listProduct) {
        // Stream through products to calculate total value
        return listProduct.stream()
                .mapToDouble(product -> product.getPrice() * product.getQuantity())
                .sum();
    }

    // Find the product with the highest price
    public static String findTheMostExpensiveProduct(ArrayList<Product> listProduct) {
        // Stream through products to find the product with the highest price
        return listProduct.stream()
                .max(Comparator.comparingDouble(Product::getPrice)) // Find the product with the max price
                .map(Product::getName) // Get the name of the most expensive product
                .orElse("No products available"); // Handle the case when the list is empty
    }

    // Check if a product is in stock by name
    public static boolean checkNameProductInStock(ArrayList<Product> listProduct, String productName) {
        // Stream through products to check if any product matches the name and has quantity > 0
        return listProduct.stream()
                .anyMatch(product -> product.getName().equalsIgnoreCase(productName) && product.getQuantity() > 0);
    }

    // Sort the product list by price or quantity
    public static ArrayList<Product> sortProductList(ArrayList<Product> listProduct, String key, String target) {
        Comparator<Product> comparator;

        // Determine the sorting criterion based on the key (price or quantity)
        switch (key.toLowerCase()) {
            case "price":
                comparator = Comparator.comparingDouble(Product::getPrice);
                break;
            case "quantity":
                comparator = Comparator.comparingInt(Product::getQuantity);
                break;
            default:
                System.out.println("Invalid sorting criteria!");
                return listProduct;
        }

        // Adjust the sorting order based on the target (ascending or descending)
        if (target.equalsIgnoreCase("DESC")) {
            comparator = comparator.reversed();
        }

        // Sort the original list in place
        listProduct.sort(comparator);
        return listProduct;
    }

}