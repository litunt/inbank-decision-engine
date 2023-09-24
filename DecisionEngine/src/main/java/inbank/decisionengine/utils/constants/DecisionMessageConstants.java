package inbank.decisionengine.utils.constants;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class DecisionMessageConstants {

  public static final String VALIDATION_ERR_MIN_VALUE = "Value is less than allowed.";
  public static final String VALIDATION_ERR_MAX_VALUE = "Value is greater than allowed.";
  public static final String VALIDATION_ERR_EMPTY = "Value is not present.";

}
