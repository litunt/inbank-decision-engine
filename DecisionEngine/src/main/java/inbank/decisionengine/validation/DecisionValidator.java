package inbank.decisionengine.validation;

import static inbank.decisionengine.utils.constants.DecisionValueConstants.MAX_ALLOWED_LOAN;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.MAX_PERIOD;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.MIN_ALLOWED_LOAN;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.MIN_PERIOD;
import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static java.lang.String.format;
import static java.math.BigDecimal.valueOf;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

import inbank.decisionengine.dto.LoanDecisionDto;
import inbank.decisionengine.dto.LoanRequestDto;
import inbank.decisionengine.exceptions.DecisionEngineRestClientException;
import inbank.decisionengine.exceptions.LoanDecisionMoneyAmountGreaterThanAllowedValidationException;
import inbank.decisionengine.exceptions.LoanDecisionMoneyAmountLessThanAllowedValidationException;
import inbank.decisionengine.exceptions.LoanDecisionPeriodGreaterThanAllowedValidationException;
import inbank.decisionengine.exceptions.LoanDecisionPeriodLessThanAllowedValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
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

  public static void validateRequest(LoanRequestDto loanRequest) {
    var errors = new ArrayList<>(validator.validate(loanRequest));
    if (isNotEmpty(errors)) {
      throwDecisionEngineServiceError(errors);
    }
  }

  public static void validateDecision(LoanDecisionDto loanDecision) {
    validateLoanMoneyAmount(loanDecision.allowedMoneyAmount());
    validateLoanPeriodLength(loanDecision.periodLength());
  }

  public static void validateLoanMoneyAmount(BigDecimal loanAmount) {
    if (loanAmount.compareTo(valueOf(MIN_ALLOWED_LOAN)) < 0) {
      throw new LoanDecisionMoneyAmountLessThanAllowedValidationException(format("Loan amount is less than allowed (%d). Actual amount: %f", MIN_ALLOWED_LOAN, loanAmount.doubleValue()));
    } else if (loanAmount.compareTo(valueOf(MAX_ALLOWED_LOAN)) > 0) {
      throw new LoanDecisionMoneyAmountGreaterThanAllowedValidationException(format("Loan amount is greater than allowed (%d). Actual amount: %f", MAX_ALLOWED_LOAN, loanAmount.doubleValue()));
    }
  }

  public static void validateLoanPeriodLength(long periodLength) {
    if (periodLength < MIN_PERIOD) {
      throw new LoanDecisionPeriodLessThanAllowedValidationException(format("Loan period length is less than allowed (%d months). Actual period length: %d", MIN_PERIOD, periodLength));
    } else if (periodLength > MAX_PERIOD) {
      throw new LoanDecisionPeriodGreaterThanAllowedValidationException(format("Loan period length is greater than allowed (%d months). Actual period length: %d", MAX_PERIOD, periodLength));
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
