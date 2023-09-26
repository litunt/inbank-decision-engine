package inbank.decisionengine.engine.handler;

import static inbank.decisionengine.enums.LoanDecisionType.ADJUSTED;
import static inbank.decisionengine.enums.LoanDecisionType.DECLINED;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.MAX_PERIOD;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.MIN_ALLOWED_LOAN;
import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang3.compare.ComparableUtils.is;

import inbank.decisionengine.dto.LoanDecisionDto;
import inbank.decisionengine.dto.loanrequest.LoanRequest;
import org.springframework.stereotype.Component;

@Component
public class CompleteRejectionLoanHandler extends LoanHandlerBase {

  @Override
  public void setNextHandler(LoanHandler handler) {
    /* this is supposed to be the last handler in the chain */
  }

  @Override
  public LoanDecisionDto handle(LoanRequest loanRequest) {
    if (isOutOfConditions(loanRequest)) {
      /*
      since we already tried modifying both amount and period, there is no options left and
      therefore if critical values are still out of constraints we can decline
      */
      return new LoanDecisionDto(DECLINED, 0, ZERO);
    }
    return new LoanDecisionDto(
        ADJUSTED,
        loanRequest.getCondition().getPeriodInMonths(),
        loanRequest.getCondition().getLoanAmount()
    );
  }

  private boolean isOutOfConditions(LoanRequest loanRequest) {
    return is(loanRequest.getCondition().getLoanAmount()).lessThan(MIN_ALLOWED_LOAN)
        || is(loanRequest.getCondition().getPeriodInMonths()).greaterThan(MAX_PERIOD);
  }
}
