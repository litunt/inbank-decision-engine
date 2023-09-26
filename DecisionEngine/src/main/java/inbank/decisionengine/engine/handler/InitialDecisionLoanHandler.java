package inbank.decisionengine.engine.handler;

import static inbank.decisionengine.engine.DecisionCalculator.calculateCreditScore;
import static inbank.decisionengine.engine.DecisionCalculator.calculateLoanAmount;
import static inbank.decisionengine.enums.LoanDecisionType.APPROVED;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.CREDIT_SCORE_FOR_LARGEST_AMOUNT;
import static org.apache.commons.lang3.compare.ComparableUtils.is;

import inbank.decisionengine.dto.LoanDecisionDto;
import inbank.decisionengine.dto.loanrequest.LoanRequest;
import org.springframework.stereotype.Component;

@Component
public class InitialDecisionLoanHandler extends LoanHandlerBase {

  @Override
  public void setNextHandler(LoanHandler handler) {
    this.nextLoanHandler = handler;
  }

  @Override
  public LoanDecisionDto handle(LoanRequest loanRequest) {
    final var creditScore = calculateCreditScore(
        loanRequest.getProfile().creditModifier(),
        loanRequest.getCondition().getLoanAmount(),
        loanRequest.getCondition().getPeriodInMonths()
    );
    /*
    if score is equal to 1, then it means that the largest allowed loan amount is chosen
    if score is larger than 1, then it means that larger loan amount can be allowed
    if score is smaller than 1, then it means that smaller amount should be found
     */
    if (is(creditScore).equalTo((double) CREDIT_SCORE_FOR_LARGEST_AMOUNT)) {
      /*
      the best case to have - the largest possible loan amount was requested and is ok
      according to conditions, can simply proceed
      */
      return new LoanDecisionDto(
          APPROVED,
          loanRequest.getCondition().getPeriodInMonths(),
          loanRequest.getCondition().getLoanAmount()
      );
    }
    /*
    case that needs extra validation, because calculated amount may get out of constraints,
    therefore additional validation step is added here
     */
    final var largestAllowedAmount = calculateLoanAmount(
        loanRequest.getProfile().creditModifier(),
        CREDIT_SCORE_FOR_LARGEST_AMOUNT,
        loanRequest.getCondition().getPeriodInMonths()
    );
    loanRequest.setConditionLoanAmount(largestAllowedAmount);
    return this.handleNextAndReturn(loanRequest);
  }
}
