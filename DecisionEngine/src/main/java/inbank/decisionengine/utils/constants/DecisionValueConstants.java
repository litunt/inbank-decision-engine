package inbank.decisionengine.utils.constants;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class DecisionValueConstants {

  public static final int MIN_ALLOWED_LOAN = 2000;
  public static final int MAX_ALLOWED_LOAN = 10000;
  public static final int MIN_PERIOD = 12;
  public static final int MAX_PERIOD = 60;
  public static final int CREDIT_SCORE_FOR_LARGEST_AMOUNT = 1;

}
