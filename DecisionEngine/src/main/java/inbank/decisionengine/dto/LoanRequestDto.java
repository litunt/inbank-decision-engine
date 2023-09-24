package inbank.decisionengine.dto;

import static inbank.decisionengine.utils.constants.DecisionMessageConstants.VALIDATION_ERR_EMPTY;
import static inbank.decisionengine.utils.constants.DecisionMessageConstants.VALIDATION_ERR_MAX_VALUE;
import static inbank.decisionengine.utils.constants.DecisionMessageConstants.VALIDATION_ERR_MIN_VALUE;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.MAX_ALLOWED_LOAN;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.MAX_PERIOD;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.MIN_ALLOWED_LOAN;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.MIN_PERIOD;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record LoanRequestDto (
    @NotBlank(message = VALIDATION_ERR_EMPTY)
    String userIdCode,
    @NotNull(message = VALIDATION_ERR_EMPTY)
    @Min(value = MIN_PERIOD, message = VALIDATION_ERR_MIN_VALUE)
    @Max(value = MAX_PERIOD, message = VALIDATION_ERR_MAX_VALUE)
    Integer periodLength,
    @NotNull(message = VALIDATION_ERR_EMPTY)
    @Min(value = MIN_ALLOWED_LOAN, message = VALIDATION_ERR_MIN_VALUE)
    @Max(value = MAX_ALLOWED_LOAN, message = VALIDATION_ERR_MAX_VALUE)
    BigDecimal requiredMoneyAmount
) {

}
