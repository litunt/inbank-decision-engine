package inbank.decisionengine.engine.handler;

import static inbank.decisionengine.engine.DecisionCalculator.calculateLoanAmount;
import static inbank.decisionengine.enums.LoanDecisionType.ADJUSTED;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.CREDIT_SCORE_FOR_LARGEST_AMOUNT;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.MAX_PERIOD;
import static org.apache.commons.lang3.compare.ComparableUtils.is;

import inbank.decisionengine.dto.LoanDecisionDto;
import inbank.decisionengine.dto.loanrequest.LoanRequest;
import org.springframework.stereotype.Component;

@Component
public class MaxPeriodLoanHandler extends LoanHandlerBase {

  @Override
  public void setNextHandler(LoanHandler handler) {
    this.nextLoanHandler = handler;
  }

  @Override
  public LoanDecisionDto handle(LoanRequest loanRequest) {
    if (is(loanRequest.getCondition().getPeriodInMonths()).lessThanOrEqualTo(MAX_PERIOD)) {
      return new LoanDecisionDto(
          ADJUSTED,
          loanRequest.getCondition().getPeriodInMonths(),
          loanRequest.getCondition().getLoanAmount()
      );
    }
    /*
    we need to find the loan amount with which it is possible to use that within maximum
    allowed periodInMonths
    */
    final var newAmount = calculateLoanAmount(loanRequest.getProfile().creditModifier(), CREDIT_SCORE_FOR_LARGEST_AMOUNT, MAX_PERIOD);
    loanRequest.setConditionLoanAmount(newAmount);
    loanRequest.setConditionPeriod(MAX_PERIOD);
    return this.handleNextAndReturn(loanRequest);
  }
}
