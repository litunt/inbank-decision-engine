package inbank.decisionengine.handlers;

import static inbank.decisionengine.TestUtils.createLoanRequestHasDebt;
import static inbank.decisionengine.TestUtils.createLoanRequestCreditModifier100;
import static inbank.decisionengine.enums.LoanDecisionType.DECLINED;
import static inbank.decisionengine.enums.LoanDecisionType.FAILURE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import inbank.decisionengine.engine.handler.HasDebtLoanHandler;
import inbank.decisionengine.engine.handler.LoanHandler;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class HasDebtLoanHandlerTest {

  private final HasDebtLoanHandler hasDebtLoanHandler = new HasDebtLoanHandler();

  @Test
  @DisplayName("Test has debt should return declined decision.")
  void testHasDebtResultDeclined() {
    final var decision = hasDebtLoanHandler.handle(createLoanRequestHasDebt());
    assertThat(decision.decision()).isEqualTo(DECLINED);
    assertThat(decision.approvedLoanAmount()).isZero();
    assertThat(decision.approvedPeriodLength()).isZero();
  }

  @Test
  @DisplayName("Test no debt should call next handler.")
  void testNoDebtNextHandlerCalled() {
    final var loanRequest = createLoanRequestCreditModifier100(20, BigDecimal.valueOf(3000));
    LoanHandler nextHandler = Mockito.mock(LoanHandler.class);
    hasDebtLoanHandler.setNextHandler(nextHandler);

    hasDebtLoanHandler.handle(loanRequest);
    verify(nextHandler).handle(loanRequest);
  }

  @Test
  @DisplayName("Test no debt next handler not set returns failure.")
  void testNoDebtNextHandlerNotSetFailure() {
    final var loanRequest = createLoanRequestCreditModifier100(20, BigDecimal.valueOf(3000));
    final var decision = hasDebtLoanHandler.handle(loanRequest);
    assertThat(decision.decision()).isEqualTo(FAILURE);
    assertThat(decision.approvedLoanAmount()).isZero();
    assertThat(decision.approvedPeriodLength()).isZero();
  }

}
