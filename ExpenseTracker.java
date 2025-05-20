import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class ExpenseTracker {
    static List<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n Expense Tracke ");
            System.out.println("1.Add Single Transaction");
            System.out.println("2.Load Transactions from File");
            System.out.print("Choose an option (or any other key to exit): ");
            String option = scanner.nextLine();

            if (option.equals("1")) {
                addTransaction(scanner);
                printSummary();
            } else if (option.equals("2")) {
                System.out.print("Enter filename to load transactions from: ");
                String fileName = scanner.nextLine().trim();

                boolean loaded = loadFromFile(fileName);
                if (loaded) {
                    System.out.println("Transactions loaded from file: " + fileName);
                } else {
                    System.out.println("File not found or contains invalid data: " + fileName);
                }
                printSummary();
            } else {
                System.out.println("Exiting....!");
                break;
            }
        }
    }

    public static void addTransaction(Scanner scanner) {
    	 System.out.print("Enter type (income/expense): ");
         String type = scanner.nextLine().trim().toLowerCase();

         String category = "";
         if (type.equals("income")) {
             System.out.print("Enter income category (salary/business): ");
             category = scanner.nextLine().trim();
         } else if (type.equals("expense")) {
             System.out.print("Enter expense category (food/rent/travel): ");
             category = scanner.nextLine().trim();
         } else {
             System.out.println("Invalid input");
             return;
         }
        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        LocalDate date = LocalDate.now();

        Transaction t = new Transaction(type, category, amount, date);
        transactions.add(t);
        System.out.println(" Transaction added: " + t);
    }

    public static boolean loadFromFile(String fileName) {
        List<Transaction> loadedTransactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("Reading line: " + line); 

                String[] parts = line.split(",");
                if (parts.length != 4) continue;

                String type = parts[0].trim().toLowerCase();
                String category = parts[1].trim();
                double amount = Double.parseDouble(parts[2].trim());
                LocalDate date = LocalDate.parse(parts[3].trim());

                if (!type.equals("income") && !type.equals("expense")) continue;

                loadedTransactions.add(new Transaction(type, category, amount, date));
            }

            if (loadedTransactions.isEmpty()) return false;

            transactions.addAll(loadedTransactions);
            return true;
        } catch (Exception e) {
            e.printStackTrace(); 
            return false;
        }
    }


    public static void printSummary() {
        double incomeTotal = 0;
        double expenseTotal = 0;

        for (Transaction t : transactions) {
            if (t.getType().equals("income")) {
                incomeTotal += t.getAmount();
            } else if (t.getType().equals("expense")) {
                expenseTotal += t.getAmount();
            }
        }

        System.out.println("\n--- Monthly Summary ---");
        System.out.printf("Income Total  : ₹%.2f%n", incomeTotal);
        System.out.printf("Expense Total : ₹%.2f%n", expenseTotal);
        System.out.printf("Balance       : ₹%.2f%n", (incomeTotal - expenseTotal));
    }
}
