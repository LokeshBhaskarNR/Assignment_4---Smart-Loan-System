package smart_loan_system;

public class HomeLoan extends LoanApplication implements Approvable {

    public HomeLoan(String customerName, double amount) {
        super(customerName, amount, LoanType.HOME, LoanStatus.PENDING);
    }

    @Override
    public boolean validateApplication() throws LoanException {
        if (getCustomerName() == null || getCustomerName().trim().isEmpty()) {
            throw new LoanException("Customer name is required for HomeLoan.");
        }
        if (getAmount() <= 0) {
            throw new LoanException("Amount must be positive for HomeLoan.");
        }

        return true;
    }

    @Override
    @AuditLog
    public LoanStatus evaluateRisk() {
        
        return (getAmount() < 5_000_000) ? LoanStatus.APPROVED : LoanStatus.REJECTED;
    }
}
