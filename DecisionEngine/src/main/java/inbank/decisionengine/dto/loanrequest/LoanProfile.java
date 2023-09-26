package inbank.decisionengine.dto.loanrequest;

public record LoanProfile(
    Boolean hasDebt,
    Integer creditModifier
) {

}
