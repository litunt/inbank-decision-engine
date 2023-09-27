package inbank.decisionengine.dto;

import inbank.decisionengine.enums.LoanDecisionType;
import java.math.BigDecimal;

public record LoanDecisionDto(
    LoanDecisionType decision,
    Integer approvedPeriodLength,
    BigDecimal approvedLoanAmount
) {

}
