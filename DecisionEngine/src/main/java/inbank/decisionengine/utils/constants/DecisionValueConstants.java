package inbank.decisionengine.utils.constants;

import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class DecisionValueConstants {

  public static final int MIN_ALLOWED_LOAN_INT = 2000;
  public static final int MAX_ALLOWED_LOAN_INT = 10000;
  public static final BigDecimal MIN_ALLOWED_LOAN = BigDecimal.valueOf(MIN_ALLOWED_LOAN_INT);
  public static final BigDecimal MAX_ALLOWED_LOAN = BigDecimal.valueOf(MAX_ALLOWED_LOAN_INT);
  public static final int MIN_PERIOD = 12;
  public static final int MAX_PERIOD = 60;
  public static final int CREDIT_SCORE_FOR_LARGEST_AMOUNT = 1;

}
