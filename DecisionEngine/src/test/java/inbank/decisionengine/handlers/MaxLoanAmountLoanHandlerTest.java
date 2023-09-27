package inbank.decisionengine.handlers;

import static inbank.decisionengine.TestUtils.createLoanRequestCreditModifier100;
import static inbank.decisionengine.TestUtils.createLoanRequestCreditModifier300;
import static inbank.decisionengine.enums.LoanDecisionType.ADJUSTED;
import static inbank.decisionengine.enums.LoanDecisionType.FAILURE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import inbank.decisionengine.engine.handler.LoanHandler;
import inbank.decisionengine.engine.handler.MaxLoanAmountLoanHandler;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MaxLoanAmountLoanHandlerTest {

  private final MaxLoanAmountLoanHandler maxLoanAmountLoanHandler = new MaxLoanAmountLoanHandler();

  @Test
  @DisplayName("Test loan amount greater than allowed maximum decision adjusted approved loan amount 10000.")
  void testLoanAmountGreaterThanAllowedMaximumDecisionAdjusted() {
    final var period = 20;
    final var loanRequest = createLoanRequestCreditModifier300(period, BigDecimal.valueOf(11000));
    final var decision = maxLoanAmountLoanHandler.handle(loanRequest);
    assertThat(decision.decision()).isEqualTo(ADJUSTED);
    assertThat(decision.approvedLoanAmount()).isEqualTo(BigDecimal.valueOf(10000));
    assertThat(decision.approvedPeriodLength()).isEqualTo(period);
  }

  @Test
  @DisplayName("Test loan amount less than allowed maximum should proceed handling call next handler.")
  void testLoanAmountLessThanAllowedMaximumShouldProceedHandlingNextHandlerCalled() {
    final var loanRequest = createLoanRequestCreditModifier100(20, BigDecimal.valueOf(3000));
    LoanHandler nextHandler = Mockito.mock(LoanHandler.class);
    maxLoanAmountLoanHandler.setNextHandler(nextHandler);

    maxLoanAmountLoanHandler.handle(loanRequest);
    verify(nextHandler).handle(loanRequest);
  }

  @Test
  @DisplayName("Test loan amount less than allowed maximum should proceed handling next handler not set returns failure.")
  void testLoanAmountLessThanAllowedMaximumShouldProceedHandlingNextHandlerNotSetFailure() {
    final var loanRequest = createLoanRequestCreditModifier100(20, BigDecimal.valueOf(2000));
    final var decision = maxLoanAmountLoanHandler.handle(loanRequest);
    assertThat(decision.decision()).isEqualTo(FAILURE);
    assertThat(decision.approvedLoanAmount()).isZero();
    assertThat(decision.approvedPeriodLength()).isZero();
  }
}
