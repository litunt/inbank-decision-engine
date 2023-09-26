package inbank.decisionengine.engine.handler;

import static inbank.decisionengine.enums.LoanDecisionType.ADJUSTED;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.MAX_ALLOWED_LOAN;
import static org.apache.commons.lang3.compare.ComparableUtils.is;

import inbank.decisionengine.dto.LoanDecisionDto;
import inbank.decisionengine.dto.loanrequest.LoanRequest;
import org.springframework.stereotype.Component;

@Component
public class MaxLoanAmountLoanHandler extends LoanHandlerBase {

  @Override
  public void setNextHandler(LoanHandler handler) {
    this.nextLoanHandler = handler;
  }

  @Override
  public LoanDecisionDto handle(LoanRequest loanRequest) {
    if (is(loanRequest.getCondition().getLoanAmount()).greaterThanOrEqualTo(MAX_ALLOWED_LOAN)) {
      /*
      we can set the largest allowed money amount here, because the calculated allowed loan is already
      larger than overall acceptable maximum
       */
      return new LoanDecisionDto(
          ADJUSTED,
          loanRequest.getCondition().getPeriodInMonths(),
          MAX_ALLOWED_LOAN
      );
    }
    return this.handleNextAndReturn(loanRequest);
  }
}
