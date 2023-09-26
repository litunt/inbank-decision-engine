package inbank.decisionengine.engine.handler;

import static inbank.decisionengine.enums.LoanDecisionType.FAILURE;
import static java.math.BigDecimal.ZERO;

import inbank.decisionengine.dto.LoanDecisionDto;
import inbank.decisionengine.dto.loanrequest.LoanRequest;

public abstract class LoanHandlerBase implements LoanHandler {

  protected LoanHandler nextLoanHandler;

  protected LoanDecisionDto handleNextAndReturn(LoanRequest loanRequest) {
    if (this.nextLoanHandler == null) {
      return new LoanDecisionDto(FAILURE, 0, ZERO);
    }
    return this.nextLoanHandler.handle(loanRequest);
  }

}
