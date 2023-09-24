package inbank.decisionengine.exceptions;

public class LoanDecisionPeriodGreaterThanAllowedValidationException extends LoanDecisionValidationException {

  public LoanDecisionPeriodGreaterThanAllowedValidationException(String message) {
    super(message);
  }
}
