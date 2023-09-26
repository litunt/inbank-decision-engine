package inbank.decisionengine.engine.handler;

import static inbank.decisionengine.engine.DecisionCalculator.calculateLoanPeriod;
import static inbank.decisionengine.enums.LoanDecisionType.ADJUSTED;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.CREDIT_SCORE_FOR_LARGEST_AMOUNT;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.MIN_ALLOWED_LOAN;
import static org.apache.commons.lang3.compare.ComparableUtils.is;

import inbank.decisionengine.dto.LoanDecisionDto;
import inbank.decisionengine.dto.loanrequest.LoanRequest;
import org.springframework.stereotype.Component;

@Component
public class MinLoanAmountLoanHandler extends LoanHandlerBase {

  @Override
  public void setNextHandler(LoanHandler handler) {
    this.nextLoanHandler = handler;
  }

  @Override
  public LoanDecisionDto handle(LoanRequest loanRequest) {
    if (is(loanRequest.getCondition().getLoanAmount()).greaterThanOrEqualTo(MIN_ALLOWED_LOAN)) {
      return new LoanDecisionDto(
          ADJUSTED,
          loanRequest.getCondition().getPeriodInMonths(),
          loanRequest.getCondition().getLoanAmount()
      );
    }
    /*
    we need to find the periodInMonths in which it is possible to use at least the min allowed loan amount
    and when the credit score gives the largest allowed amount, so is equal to 1
    */
    final var newPeriod = calculateLoanPeriod(CREDIT_SCORE_FOR_LARGEST_AMOUNT, loanRequest.getProfile().creditModifier(), MIN_ALLOWED_LOAN);
    loanRequest.setConditionPeriod(newPeriod);
    loanRequest.setConditionLoanAmount(MIN_ALLOWED_LOAN);
    return this.handleNextAndReturn(loanRequest);
  }
}
