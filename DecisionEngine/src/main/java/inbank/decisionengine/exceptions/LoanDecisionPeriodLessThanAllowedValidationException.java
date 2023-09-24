package inbank.decisionengine.exceptions;

public class LoanDecisionPeriodLessThanAllowedValidationException extends
    LoanDecisionValidationException {

  public LoanDecisionPeriodLessThanAllowedValidationException(String message) {
    super(message);
  }
}
