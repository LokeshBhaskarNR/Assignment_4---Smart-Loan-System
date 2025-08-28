package smart_loan_system;

import java.lang.reflect.*;
import java.util.*;


public class SmartLoanMain {

    private static final Scanner sc = new Scanner(System.in);
    private static final Map<LoanType, List<LoanApplication>> processed = new HashMap<>();
    private static final List<LoanApplication> failed = new ArrayList<>();

    public static <T extends LoanApplication> void printApplications(List<T> apps, String title) {
        System.out.println("\n=== " + title + " ===");
        if (apps.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        for (T app : apps) {
            System.out.println(app);
        }
    }


    private static void store(LoanApplication app) {
        processed.computeIfAbsent(app.getType(), k -> new ArrayList<>()).add(app);
    }

    private static void printAuditLoggedMethods(Class<?>... classes) {
        System.out.println("\n=== Audit Logged Methods ===");
        for (Class<?> cls : classes) {
            for (Method m : cls.getDeclaredMethods()) {
                if (m.isAnnotationPresent(AuditLog.class)) {
                    System.out.println(cls.getSimpleName() + "." + m.getName() + "()");
                }
            }
        }
    }

    private static void processLoan(LoanApplication app) {
        LoanProcessor pipeline = (LoanApplication application) -> {
            if (application.validateApplication()) {
                application.apply();
                if (application instanceof Approvable) {
                    ((Approvable) application).printDecision();
                } else {
                    throw new LoanException("Not approvable type.");
                }
            }
        };

        try {
            pipeline.process(app);
            store(app);
        } catch (LoanException ex) {
            System.out.println("Validation/Processing failed for #" + app.getApplicationId() +
                    " (" + app.getClass().getSimpleName() + "): " + ex.getMessage());
            failed.add(app);
        } catch (Exception ex) {
            System.out.println("Unexpected error for #" + app.getApplicationId() + ": " + ex.getMessage());
            failed.add(app);
        }
    }

    private static void applyHomeLoan() {
        System.out.print("Enter customer name: ");
        String name = sc.nextLine();
        System.out.print("Enter loan amount (₹): ");
        double amount = sc.nextDouble();
        sc.nextLine(); // consume newline
        LoanApplication loan = new HomeLoan(name, amount);
        processLoan(loan);
    }

    private static void applyPersonalLoan() {
        System.out.print("Enter customer name: ");
        String name = sc.nextLine();
        System.out.print("Enter loan amount (₹): ");
        double amount = sc.nextDouble();
        sc.nextLine(); // consume newline
        LoanApplication loan = new PersonalLoan(name, amount);
        processLoan(loan);
    }

    private static void viewProcessed() {
        if (processed.isEmpty()) {
            System.out.println("\nNo processed applications yet.");
            return;
        }
        Comparator<LoanApplication> byAmountThenName =
                Comparator.comparingDouble(LoanApplication::getAmount)
                          .thenComparing(LoanApplication::getCustomerName, String.CASE_INSENSITIVE_ORDER);

        for (Map.Entry<LoanType, List<LoanApplication>> e : processed.entrySet()) {
            e.getValue().sort(byAmountThenName);
            printApplications(e.getValue(), "Processed " + e.getKey() + " Applications");
        }
    }

    private static void viewFailed() {
        printApplications(failed, "Failed/Invalid Applications");
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Smart Loan System ===");
            System.out.println("1. Apply for Home Loan");
            System.out.println("2. Apply for Personal Loan");
            System.out.println("3. View Processed Applications");
            System.out.println("4. View Failed Applications");
            System.out.println("5. View Audit Logged Methods");
            System.out.println("6. Exit"
            		+ "\n");
            System.out.print("Enter Your Choice : "
            		+ "\n");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input !");
                continue;
            }

            switch (choice) {
                case 1 -> applyHomeLoan();
                case 2 -> applyPersonalLoan();
                case 3 -> viewProcessed();
                case 4 -> viewFailed();
                case 5 -> printAuditLoggedMethods(HomeLoan.class, PersonalLoan.class);
                case 6 -> {
                    System.out.println("Exiting Smart Loan System");
                    return;
                }
                default -> System.out.println("Invalid choice !");
            }
        }
    }
}
