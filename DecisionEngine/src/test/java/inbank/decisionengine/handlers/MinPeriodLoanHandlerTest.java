package inbank.decisionengine.handlers;

import static inbank.decisionengine.TestUtils.createLoanRequestCreditModifier100;
import static inbank.decisionengine.TestUtils.createLoanRequestCreditModifier300;
import static inbank.decisionengine.enums.LoanDecisionType.ADJUSTED;
import static inbank.decisionengine.enums.LoanDecisionType.FAILURE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import inbank.decisionengine.engine.handler.LoanHandler;
import inbank.decisionengine.engine.handler.MinPeriodLoanHandler;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MinPeriodLoanHandlerTest {

  private final MinPeriodLoanHandler minPeriodLoanHandler = new MinPeriodLoanHandler();

  @Test
  @DisplayName("Test period length less than allowed minimum decision adjusted period length set to 12.")
  void testPeriodLengthLessThanAllowedMinimumDecisionAdjusted() {
    final var amount = BigDecimal.valueOf(5000);
    final var loanRequest = createLoanRequestCreditModifier300(10, amount);
    final var decision = minPeriodLoanHandler.handle(loanRequest);
    assertThat(decision.decision()).isEqualTo(ADJUSTED);
    assertThat(decision.approvedLoanAmount()).isEqualTo(amount);
    assertThat(decision.approvedPeriodLength()).isEqualTo(12);
  }

  @Test
  @DisplayName("Test period length greater than allowed minimum should proceed handling call next handler.")
  void testPeriodLengthGreaterThanAllowedMinimumShouldProceedHandlingNextHandlerCalled() {
    final var loanRequest = createLoanRequestCreditModifier100(15, BigDecimal.valueOf(1000));
    LoanHandler nextHandler = Mockito.mock(LoanHandler.class);
    minPeriodLoanHandler.setNextHandler(nextHandler);

    minPeriodLoanHandler.handle(loanRequest);
    verify(nextHandler).handle(loanRequest);
  }

  @Test
  @DisplayName("Test period length greater than allowed minimum should proceed handling next handler not set returns failure.")
  void testPeriodLengthGreaterThanAllowedMinimumShouldProceedHandlingNextHandlerNotSetFailure() {
    final var loanRequest = createLoanRequestCreditModifier100(15, BigDecimal.valueOf(1000));
    final var decision = minPeriodLoanHandler.handle(loanRequest);
    assertThat(decision.decision()).isEqualTo(FAILURE);
    assertThat(decision.approvedLoanAmount()).isZero();
    assertThat(decision.approvedPeriodLength()).isZero();
  }

}
