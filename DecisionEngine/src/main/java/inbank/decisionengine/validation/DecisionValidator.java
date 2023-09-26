package inbank.decisionengine.validation;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

import inbank.decisionengine.dto.LoanUserRequestDto;
import inbank.decisionengine.exceptions.DecisionEngineRestClientException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class DecisionValidator {

  private static final Validator validator;
  private static final String ERROR_MSG_TEMPLATE = "%s invalid: %s";

  static {
    try (var validatorFactory = buildDefaultValidatorFactory()) {
      validator = validatorFactory.getValidator();
    }
  }

  public static void validateRequest(LoanUserRequestDto loanRequest) {
    var errors = new ArrayList<>(validator.validate(loanRequest));
    if (isNotEmpty(errors)) {
      throwDecisionEngineServiceError(errors);
    }
  }

  private static <T> void throwDecisionEngineServiceError(List<ConstraintViolation<T>> errors) {
    logErrors(errors);
    throw new DecisionEngineRestClientException(format(ERROR_MSG_TEMPLATE, errors.get(0).getPropertyPath().toString(), errors.get(0).getInvalidValue()), errors.get(0).getMessage());
  }

  private static <T> void logErrors(List<ConstraintViolation<T>> errors) {
    errors.forEach(e -> log.error(format(ERROR_MSG_TEMPLATE, e.getPropertyPath().toString(), e.getInvalidValue())));
  }

}
