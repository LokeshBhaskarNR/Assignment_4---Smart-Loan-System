package smart_loan_system;

@FunctionalInterface
public interface LoanProcessor {
    void process(LoanApplication app) throws LoanException;
}
