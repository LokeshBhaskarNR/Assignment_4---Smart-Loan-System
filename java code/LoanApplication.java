package smart_loan_system;

import java.util.*;

public abstract class LoanApplication {
    private static int COUNTER = 1000;

    static {
        System.out.println("SmartLoan System initialized.");
    }

    private final int applicationId;
    private final String customerName;
    private final double amount;
    private final LoanType type;
    private LoanStatus status;

    protected LoanApplication(String customerName, double amount, LoanType type, LoanStatus initialStatus) {
        this.applicationId = ++COUNTER; // static counter
        this.customerName = customerName;
        this.amount = amount;
        this.type = Objects.requireNonNull(type, "LoanType cannot be null");
        this.status = initialStatus == null ? LoanStatus.PENDING : initialStatus;
    }

    public abstract boolean validateApplication() throws LoanException;

    public void apply() {
        this.status = LoanStatus.PENDING;
    }

    // Protected so Approvable.default can set it
    protected void setStatus(LoanStatus status) {
        this.status = status;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getAmount() {
        return amount;
    }

    public LoanType getType() {
        return type;
    }

    public LoanStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "LoanApplication{" +
                "id=" + applicationId +
                ", customer='" + customerName + '\'' +
                ", amount=₹" + String.format("%.2f", amount) +
                ", type=" + type +
                ", status=" + status +
                '}';
    }
}
