package inbank.decisionengine.handlers;

import static inbank.decisionengine.TestUtils.createLoanRequestCreditModifier100;
import static inbank.decisionengine.TestUtils.createLoanRequestCreditModifier300;
import static inbank.decisionengine.enums.LoanDecisionType.ADJUSTED;
import static inbank.decisionengine.enums.LoanDecisionType.FAILURE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.verify;

import inbank.decisionengine.engine.handler.LoanHandler;
import inbank.decisionengine.engine.handler.MaxPeriodLoanHandler;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MaxPeriodLoanHandlerTest {

  private final MaxPeriodLoanHandler maxPeriodLoanHandler = new MaxPeriodLoanHandler();

  @Test
  @DisplayName("Test period length less than allowed maximum decision adjusted.")
  void testPeriodLengthLessThanAllowedMinimumDecisionAdjusted() {
    final var period = 30;
    final var amount = BigDecimal.valueOf(5000);
    final var loanRequest = createLoanRequestCreditModifier300(period, amount);
    final var decision = maxPeriodLoanHandler.handle(loanRequest);
    assertThat(decision.decision()).isEqualTo(ADJUSTED);
    assertThat(decision.approvedLoanAmount()).isEqualTo(amount);
    assertThat(decision.approvedPeriodLength()).isEqualTo(period);
  }

  @Test
  @DisplayName("Test period length greater than allowed maximum should proceed handling amount recalculated max allowed period is set call next handler.")
  void testPeriodLengthGreaterThanAllowedMaximumShouldProceedHandlingNextHandlerCalled() {
    final var loanRequest = createLoanRequestCreditModifier100(70, BigDecimal.valueOf(7000));
    LoanHandler nextHandler = Mockito.mock(LoanHandler.class);
    maxPeriodLoanHandler.setNextHandler(nextHandler);

    maxPeriodLoanHandler.handle(loanRequest);
    verify(nextHandler).handle(loanRequest);
    assertThat(loanRequest.getCondition().getPeriodInMonths()).isEqualTo(60);
    assertThat(loanRequest.getCondition().getLoanAmount()).isCloseTo(BigDecimal.valueOf(5000), within(BigDecimal.valueOf(0.01)));
  }

  @Test
  @DisplayName("Test period length greater than allowed maximum should proceed handling next handler not set returns failure.")
  void testPeriodLengthGreaterThanAllowedMaximumShouldProceedHandlingNextHandlerNotSetFailure() {
    final var loanRequest = createLoanRequestCreditModifier100(70, BigDecimal.valueOf(1000));
    final var decision = maxPeriodLoanHandler.handle(loanRequest);
    assertThat(decision.decision()).isEqualTo(FAILURE);
    assertThat(decision.approvedLoanAmount()).isZero();
    assertThat(decision.approvedPeriodLength()).isZero();
  }
}
