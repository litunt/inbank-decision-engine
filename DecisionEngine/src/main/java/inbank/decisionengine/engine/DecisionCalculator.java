package inbank.decisionengine.engine;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;
import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class DecisionCalculator {

  public static double calculateCreditScore(int creditModifier, BigDecimal loanAmount, long loanPeriod) {
    return valueOf(creditModifier)
        .divide(loanAmount, 2, HALF_UP)
        .multiply(valueOf(loanPeriod))
        .doubleValue();
  }

  public static BigDecimal calculateLoanAmount(int creditModifier, double creditScore, long loanPeriod) {
    return valueOf(creditModifier)
        .divide(valueOf(creditScore)
            .divide(valueOf(loanPeriod), 2, HALF_UP),
            2, HALF_UP);
  }

  public static int calculateLoanPeriod(double creditScore, int creditModifier, BigDecimal loanAmount) {
    return valueOf(creditScore)
        .divide(valueOf(creditModifier)
            .divide(loanAmount, 2, HALF_UP),
            2, HALF_UP)
        .intValue();
  }

}
