package smart_loan_system;

public class PersonalLoan extends LoanApplication implements Approvable {

    public PersonalLoan(String customerName, double amount) {
        super(customerName, amount, LoanType.PERSONAL, LoanStatus.PENDING);
    }

    @Override
    public boolean validateApplication() throws LoanException {
        if (getCustomerName() == null || getCustomerName().trim().isEmpty()) {
            throw new LoanException("Customer name is required for PersonalLoan.");
        }
        if (getAmount() <= 0) {
            throw new LoanException("Amount must be positive for PersonalLoan.");
        }
        return true;
    }

    @Override
    @AuditLog
    public LoanStatus evaluateRisk() {
        // Business rule: reject if amount > ₹5 lakhs (500,000)
        return (getAmount() > 500_000) ? LoanStatus.REJECTED : LoanStatus.APPROVED;
    }
}
