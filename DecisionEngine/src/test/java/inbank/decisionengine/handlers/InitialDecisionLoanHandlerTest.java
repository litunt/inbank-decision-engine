package inbank.decisionengine.handlers;

import static inbank.decisionengine.TestUtils.createLoanRequestCreditModifier100;
import static inbank.decisionengine.TestUtils.createLoanRequestCreditModifier300;
import static inbank.decisionengine.enums.LoanDecisionType.APPROVED;
import static inbank.decisionengine.enums.LoanDecisionType.FAILURE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import inbank.decisionengine.engine.handler.InitialDecisionLoanHandler;
import inbank.decisionengine.engine.handler.LoanHandler;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class InitialDecisionLoanHandlerTest {

  private final InitialDecisionLoanHandler initialDecisionLoanHandler = new InitialDecisionLoanHandler();

  @Test
  @DisplayName("Test credit score equals 1 decision approved.")
  void testCreditScoreEqualsOneDecisionApproved() {
    final var amount = BigDecimal.valueOf(6000);
    final var period = 20;
    final var loanRequest = createLoanRequestCreditModifier300(period, amount);
    final var decision = initialDecisionLoanHandler.handle(loanRequest);
    assertThat(decision.decision()).isEqualTo(APPROVED);
    assertThat(decision.approvedLoanAmount()).isEqualTo(amount);
    assertThat(decision.approvedPeriodLength()).isEqualTo(period);
  }

  @Test
  @DisplayName("Test credit score not equal to 1 call next handler.")
  void testCreditScoreNotEqualToOneNextHandlerCalled() {
    final var loanRequest = createLoanRequestCreditModifier100(20, BigDecimal.valueOf(3000));
    LoanHandler nextHandler = Mockito.mock(LoanHandler.class);
    initialDecisionLoanHandler.setNextHandler(nextHandler);

    initialDecisionLoanHandler.handle(loanRequest);
    verify(nextHandler).handle(loanRequest);
  }

  @Test
  @DisplayName("Test credit score less than 1 should recalculate next handler not set returns failure.")
  void testCreditScoreLessThanOneShouldRecalculateNextHandlerNotSetFailure() {
    final var loanRequest = createLoanRequestCreditModifier100(20, BigDecimal.valueOf(3000));
    final var decision = initialDecisionLoanHandler.handle(loanRequest);
    assertThat(decision.decision()).isEqualTo(FAILURE);
    assertThat(decision.approvedLoanAmount()).isZero();
    assertThat(decision.approvedPeriodLength()).isZero();
  }

}
