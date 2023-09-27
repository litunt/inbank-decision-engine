package inbank.decisionengine.handlers;

import static inbank.decisionengine.TestUtils.createLoanRequestCreditModifier100;
import static inbank.decisionengine.TestUtils.createLoanRequestCreditModifier300;
import static inbank.decisionengine.enums.LoanDecisionType.ADJUSTED;
import static inbank.decisionengine.enums.LoanDecisionType.DECLINED;
import static org.assertj.core.api.Assertions.assertThat;

import inbank.decisionengine.engine.handler.CompleteRejectionLoanHandler;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CompleteRejectionLoanHandlerTest {

  private final CompleteRejectionLoanHandler completeRejectionLoanHandler = new CompleteRejectionLoanHandler();

  @Test
  @DisplayName("Test period and amount are correct decision adjusted.")
  void testPeriodAndAmountAreCorrectDecisionAdjusted() {
    final var period = 30;
    final var amount = BigDecimal.valueOf(5000);
    final var loanRequest = createLoanRequestCreditModifier300(period, amount);
    final var decision = completeRejectionLoanHandler.handle(loanRequest);
    assertThat(decision.decision()).isEqualTo(ADJUSTED);
    assertThat(decision.approvedLoanAmount()).isEqualTo(amount);
    assertThat(decision.approvedPeriodLength()).isEqualTo(period);
  }

  @Test
  @DisplayName("Test period or amount out of bounds decision declined.")
  void testPeriodOrAmountOutOfBoundsDecisionDeclined() {
    final var loanRequest = createLoanRequestCreditModifier100(70, BigDecimal.valueOf(1000));
    final var decision = completeRejectionLoanHandler.handle(loanRequest);
    assertThat(decision.decision()).isEqualTo(DECLINED);
    assertThat(decision.approvedLoanAmount()).isZero();
    assertThat(decision.approvedPeriodLength()).isZero();
  }

}
