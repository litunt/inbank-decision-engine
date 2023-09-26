package inbank.decisionengine.engine.handler;

import static inbank.decisionengine.enums.LoanDecisionType.DECLINED;
import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang3.BooleanUtils.isTrue;

import inbank.decisionengine.dto.LoanDecisionDto;
import inbank.decisionengine.dto.loanrequest.LoanRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HasDebtLoanHandler extends LoanHandlerBase {

  @Override
  public void setNextHandler(LoanHandler handler) {
    this.nextLoanHandler = handler;
  }

  @Override
  public LoanDecisionDto handle(LoanRequest loanRequest) {
    if (isTrue(loanRequest.getProfile().hasDebt())) {
      return new LoanDecisionDto(DECLINED, 0, ZERO);
    }
    return this.handleNextAndReturn(loanRequest);
  }
}
