package inbank.decisionengine.handlers;

import static inbank.decisionengine.TestUtils.createLoanRequestCreditModifier100;
import static inbank.decisionengine.TestUtils.createLoanRequestCreditModifier300;
import static inbank.decisionengine.enums.LoanDecisionType.ADJUSTED;
import static inbank.decisionengine.enums.LoanDecisionType.FAILURE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import inbank.decisionengine.engine.handler.LoanHandler;
import inbank.decisionengine.engine.handler.MinLoanAmountLoanHandler;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MinLoanAmountLoanHandlerTest {

  private final MinLoanAmountLoanHandler minLoanAmountLoanHandler = new MinLoanAmountLoanHandler();

  @Test
  @DisplayName("Test loan amount greater than allowed minimum decision adjusted.")
  void testLoanAmountGreaterThanAllowedMinimumDecisionAdjusted() {
    final var period = 20;
    final var amount = BigDecimal.valueOf(5000);
    final var loanRequest = createLoanRequestCreditModifier300(period, amount);
    final var decision = minLoanAmountLoanHandler.handle(loanRequest);
    assertThat(decision.decision()).isEqualTo(ADJUSTED);
    assertThat(decision.approvedLoanAmount()).isEqualTo(amount);
    assertThat(decision.approvedPeriodLength()).isEqualTo(period);
  }

  @Test
  @DisplayName("Test loan amount less than allowed minimum should proceed handling recalculate period and set min allowed amount call next handler.")
  void testLoanAmountLessThanAllowedMinimumShouldProceedHandlingNextHandlerCalled() {
    final var loanRequest = createLoanRequestCreditModifier100(15, BigDecimal.valueOf(1000));
    LoanHandler nextHandler = Mockito.mock(LoanHandler.class);
    minLoanAmountLoanHandler.setNextHandler(nextHandler);

    minLoanAmountLoanHandler.handle(loanRequest);
    verify(nextHandler).handle(loanRequest);
    assertThat(loanRequest.getCondition().getPeriodInMonths()).isEqualTo(20);
    assertThat(loanRequest.getCondition().getLoanAmount()).isEqualTo(BigDecimal.valueOf(2000));
  }

  @Test
  @DisplayName("Test loan amount less than allowed minimum should proceed handling next handler not set returns failure.")
  void testLoanAMountGreaterThanAllowedMinimumShouldProceedHandlingNextHandlerNotSetFailure() {
    final var loanRequest = createLoanRequestCreditModifier100(15, BigDecimal.valueOf(1000));
    final var decision = minLoanAmountLoanHandler.handle(loanRequest);
    assertThat(decision.decision()).isEqualTo(FAILURE);
    assertThat(decision.approvedLoanAmount()).isZero();
    assertThat(decision.approvedPeriodLength()).isZero();
  }
}
