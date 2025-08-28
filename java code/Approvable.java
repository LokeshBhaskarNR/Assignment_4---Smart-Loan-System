package smart_loan_system;

public interface Approvable {
    LoanStatus evaluateRisk();

    default void printDecision() {
        // By contract, classes implementing this also extend LoanApplication.
        LoanApplication app = (LoanApplication) this;
        LoanStatus result = evaluateRisk();
        app.setStatus(result);
        System.out.println("Decision for Application #" + app.getApplicationId() +
                " (" + app.getType() + ", ₹" + String.format("%.2f", app.getAmount()) + "): " + result);
    }
}
