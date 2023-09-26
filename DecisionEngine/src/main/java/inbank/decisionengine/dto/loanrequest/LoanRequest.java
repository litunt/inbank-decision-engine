package inbank.decisionengine.dto.loanrequest;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanRequest {

  private LoanCondition condition;
  private final LoanProfile profile;

  public LoanRequest(LoanCondition condition, LoanProfile profile) {
    this.condition = condition;
    this.profile = profile;
  }

  public void setConditionLoanAmount(BigDecimal newAmount) {
    this.condition.setLoanAmount(newAmount);
  }

  public void setConditionPeriod(int newPeriod) {
    this.condition.setPeriodInMonths(newPeriod);
  }

}
