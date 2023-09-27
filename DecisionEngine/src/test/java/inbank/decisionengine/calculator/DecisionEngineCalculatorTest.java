package inbank.decisionengine.calculator;

import static inbank.decisionengine.engine.DecisionCalculator.calculateCreditScore;
import static inbank.decisionengine.engine.DecisionCalculator.calculateLoanAmount;
import static inbank.decisionengine.engine.DecisionCalculator.calculateLoanPeriod;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.assertj.core.api.Assertions.within;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DecisionEngineCalculatorTest {

  private static final double PRECISION_D = 0.01;
  private static final BigDecimal PRECISION = BigDecimal.valueOf(PRECISION_D);

  @Test
  @DisplayName("Calculate Credit Score: credit modifier 750, loan amount 500, loan period 12")
  void testCalculateCreditScore1() {
    int creditModifier = 750;
    var loanAmount = BigDecimal.valueOf(5000);
    long loanPeriod = 12;

    double creditScore = calculateCreditScore(creditModifier, loanAmount, loanPeriod);

    assertThat(creditScore).isEqualTo(1.8, within(PRECISION_D));
  }

  @Test
  @DisplayName("Calculate Credit Score: credit modifier 800, loan amount 10000, loan period 24")
  void testCalculateCreditScore2() {
    int creditModifier = 800;
    var loanAmount = BigDecimal.valueOf(10000);
    long loanPeriod = 24;

    double creditScore = calculateCreditScore(creditModifier, loanAmount, loanPeriod);

    assertThat(creditScore).isEqualTo(1.92, within(PRECISION_D));
  }

  @Test
  @DisplayName("Calculate Credit Score: credit modifier 700, loan amount 3000, loan period 6")
  void testCalculateCreditScore3() {
    int creditModifier = 700;
    var loanAmount = BigDecimal.valueOf(3000);
    long loanPeriod = 6;

    double creditScore = calculateCreditScore(creditModifier, loanAmount, loanPeriod);

    assertThat(creditScore).isEqualTo(1.38, within(PRECISION_D));
  }

  @Test
  @DisplayName("Calculate Credit Score: credit modifier 100, loan amount 2000, loan period 12")
  void testCalculateCreditScore5() {
    int creditModifier = 100;
    var loanAmount = BigDecimal.valueOf(2000);
    long loanPeriod = 12;

    double creditScore = calculateCreditScore(creditModifier, loanAmount, loanPeriod);

    assertThat(creditScore).isCloseTo(0.6, offset(PRECISION_D));
  }

  @Test
  @DisplayName("Calculate Loan Amount: credit modifier 750, credit score 1.75, loan period 12")
  void testCalculateLoanAmount1() {
    int creditModifier = 750;
    double creditScore = 1.75;
    long loanPeriod = 12;

    var loanAmount = calculateLoanAmount(creditModifier, creditScore, loanPeriod);

    assertThat(loanAmount).isCloseTo(BigDecimal.valueOf(5000), within(PRECISION));
  }

  @Test
  @DisplayName("Calculate Loan Amount: credit modifier 800, credit score 1.7, loan period 24")
  void testCalculateLoanAmount2() {
    int creditModifier = 800;
    double creditScore = 1.7;
    long loanPeriod = 24;

    var loanAmount = calculateLoanAmount(creditModifier, creditScore, loanPeriod);

    assertThat(loanAmount).isCloseTo(BigDecimal.valueOf(11428.57), within(PRECISION));
  }

  @Test
  @DisplayName("Calculate Loan Amount: credit modifier 700, credit score 1, loan period 20")
  void testCalculateLoanAmount3() {
    int creditModifier = 700;
    double creditScore = 1;
    long loanPeriod = 20;

    var loanAmount = calculateLoanAmount(creditModifier, creditScore, loanPeriod);

    assertThat(loanAmount).isCloseTo(BigDecimal.valueOf(14000), within(PRECISION));
  }

  @Test
  @DisplayName("Calculate Loan Amount: credit modifier 200, credit score 0.5, loan period 12")
  void testCalculateLoanAmount4() {
    int creditModifier = 200;
    double creditScore = 0.5;
    long loanPeriod = 12;

    var loanAmount = calculateLoanAmount(creditModifier, creditScore, loanPeriod);

    assertThat(loanAmount).isCloseTo(BigDecimal.valueOf(5000), within(PRECISION));
  }

  @Test
  @DisplayName("Calculate Loan Period: credit score 1.75, credit modifier 750, loan amount 5000")
  void testCalculateLoanPeriod1() {
    double creditScore = 1.75;
    int creditModifier = 750;
    var loanAmount = BigDecimal.valueOf(5000);

    int loanPeriod = calculateLoanPeriod(creditScore, creditModifier, loanAmount);

    assertThat(loanPeriod).isEqualTo(11);
  }

  @Test
  @DisplayName("Calculate Loan Period: credit score 16.67, credit modifier 800, loan amount 10000")
  void testCalculateLoanPeriod2() {
    double creditScore = 16.67;
    int creditModifier = 800;
    var loanAmount = BigDecimal.valueOf(10000);

    int loanPeriod = calculateLoanPeriod(creditScore, creditModifier, loanAmount);

    assertThat(loanPeriod).isEqualTo(208);
  }

  @Test
  @DisplayName("Calculate Loan Period: credit score 1.5, credit modifier 700, loan amount 3000")
  void testCalculateLoanPeriod3() {
    double creditScore = 1.5;
    int creditModifier = 700;
    var loanAmount = BigDecimal.valueOf(3000);

    int loanPeriod = calculateLoanPeriod(creditScore, creditModifier, loanAmount);

    assertThat(loanPeriod).isEqualTo(6);
  }

  @Test
  @DisplayName("Calculate Loan Period: credit score 0.8, credit modifier 400, loan amount 3000")
  void testCalculateLoanPeriod4() {
    double creditScore = 0.8;
    int creditModifier = 400;
    var loanAmount = BigDecimal.valueOf(3000);

    int loanPeriod = calculateLoanPeriod(creditScore, creditModifier, loanAmount);

    assertThat(loanPeriod).isEqualTo(6);
  }

}
