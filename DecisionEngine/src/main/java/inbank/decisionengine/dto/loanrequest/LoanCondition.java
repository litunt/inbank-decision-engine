package inbank.decisionengine.dto.loanrequest;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanCondition {

  private Integer periodInMonths;
  private BigDecimal loanAmount;

  public LoanCondition(Integer periodInMonths, BigDecimal loanAmount) {
    this.periodInMonths = periodInMonths;
    this.loanAmount = loanAmount;
  }

}
