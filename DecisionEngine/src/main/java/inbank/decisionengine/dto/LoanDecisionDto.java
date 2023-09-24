package inbank.decisionengine.dto;

import inbank.decisionengine.enums.LoanDecision;
import java.math.BigDecimal;

public record LoanDecisionDto(
    String userIdCode,
    LoanDecision decision,
    Integer periodLength,
    BigDecimal allowedMoneyAmount
) {

  public LoanDecisionDto withAllowedMoneyAmount(BigDecimal money) {
    return new LoanDecisionDto(userIdCode(), decision(), periodLength(), money);
  }
  public LoanDecisionDto withPeriodLength(Integer newPeriodLength) {
    return new LoanDecisionDto(userIdCode(), decision(), newPeriodLength, allowedMoneyAmount());
  }
  public LoanDecisionDto withDecision(LoanDecision newDecision) {
    return new LoanDecisionDto(userIdCode(), newDecision, periodLength, allowedMoneyAmount());
  }

}
