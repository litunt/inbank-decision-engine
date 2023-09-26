package inbank.decisionengine.engine.handler;

import static inbank.decisionengine.enums.LoanDecisionType.ADJUSTED;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.MIN_PERIOD;
import static org.apache.commons.lang3.compare.ComparableUtils.is;

import inbank.decisionengine.dto.LoanDecisionDto;
import inbank.decisionengine.dto.loanrequest.LoanRequest;
import org.springframework.stereotype.Component;

@Component
public class MinPeriodLoanHandler extends LoanHandlerBase {

  @Override
  public void setNextHandler(LoanHandler handler) {
    this.nextLoanHandler = handler;
  }

  @Override
  public LoanDecisionDto handle(LoanRequest loanRequest) {
    if (is(loanRequest.getCondition().getPeriodInMonths()).lessThanOrEqualTo(MIN_PERIOD)) {
      /*
      we can set the shortest allowed periodInMonths here, because the calculated allowed periodInMonths is already
      less than overall acceptable minimum
      */
      return new LoanDecisionDto(
          ADJUSTED,
          MIN_PERIOD,
          loanRequest.getCondition().getLoanAmount()
      );
    }
    return this.handleNextAndReturn(loanRequest);
  }
}
