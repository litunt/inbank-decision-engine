package inbank.decisionengine.dto;

import inbank.decisionengine.enums.LoanDecisionType;
import java.math.BigDecimal;

public record LoanDecisionDto(
    LoanDecisionType decision,
    Integer approvedPeriodLength,
    BigDecimal approvedLoanAmount
) {

  public LoanDecisionDto withAllowedMoneyAmount(BigDecimal money) {
    return new LoanDecisionDto(decision(), approvedPeriodLength(), money);
  }
  public LoanDecisionDto withPeriodLength(Integer newPeriodLength) {
    return new LoanDecisionDto(decision(), newPeriodLength, approvedLoanAmount());
  }
  public LoanDecisionDto withDecision(LoanDecisionType newDecision) {
    return new LoanDecisionDto(newDecision, approvedPeriodLength, approvedLoanAmount());
  }

}
