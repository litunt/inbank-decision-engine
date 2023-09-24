package inbank.decisionengine.decisionengine;

import static inbank.decisionengine.decisionengine.DecisionCalculator.calculateLoanPeriod;
import static inbank.decisionengine.enums.LoanDecision.APPROVED;
import static inbank.decisionengine.enums.LoanDecision.DECLINED;
import static inbank.decisionengine.decisionengine.DecisionCalculator.calculateCreditScore;
import static inbank.decisionengine.decisionengine.DecisionCalculator.calculateLoanAmount;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.CREDIT_SCORE_FOR_LARGEST_AMOUNT;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.MAX_ALLOWED_LOAN;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.MAX_PERIOD;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.MIN_ALLOWED_LOAN;
import static inbank.decisionengine.utils.constants.DecisionValueConstants.MIN_PERIOD;
import static inbank.decisionengine.utils.constants.ErrorConstants.ERROR_DECISION_NOT_CREATED;
import static inbank.decisionengine.validation.DecisionValidator.validateDecision;
import static inbank.decisionengine.validation.DecisionValidator.validateLoanMoneyAmount;
import static inbank.decisionengine.validation.DecisionValidator.validateLoanPeriodLength;
import static java.lang.Boolean.TRUE;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static lombok.AccessLevel.PRIVATE;

import inbank.decisionengine.dto.LoanDecisionDto;
import inbank.decisionengine.dto.LoanRequestDto;
import inbank.decisionengine.exceptions.DecisionEngineRestClientException;
import inbank.decisionengine.exceptions.LoanDecisionMoneyAmountGreaterThanAllowedValidationException;
import inbank.decisionengine.exceptions.LoanDecisionMoneyAmountLessThanAllowedValidationException;
import inbank.decisionengine.exceptions.LoanDecisionPeriodGreaterThanAllowedValidationException;
import inbank.decisionengine.exceptions.LoanDecisionPeriodLessThanAllowedValidationException;
import inbank.decisionengine.exceptions.LoanDecisionValidationException;
import inbank.decisionengine.model.UserLoanProfile;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class DecisionEngine {

  public static LoanDecisionDto calculateDecision(LoanRequestDto loanRequest, UserLoanProfile userProfile) {
    if (TRUE.equals(userProfile.getHasDebt())) {
      log.info("User with id code {} has a debt, loan request declined.", userProfile.getIdCode());
      return getHasDebtDeclinedDecision(loanRequest);
    }
    var loanDecision = getInitialLoanDecision(loanRequest, userProfile);
    if (loanDecision.isPresent()) {
      return validateAndProceed(loanDecision.get(), userProfile.getLoanSegmentation().getCreditModifier());
    } else {
      throw new DecisionEngineRestClientException("Could not compose loan decision", ERROR_DECISION_NOT_CREATED);
    }
  }

  private static Optional<LoanDecisionDto> getInitialLoanDecision(LoanRequestDto loanRequest, UserLoanProfile userProfile) {
    final var creditModifier = userProfile.getLoanSegmentation().getCreditModifier();
    final var creditScore = calculateCreditScore(creditModifier, loanRequest.requiredMoneyAmount(), loanRequest.periodLength());
    return getLoanDecision(loanRequest, creditModifier, creditScore);
  }

  private static LoanDecisionDto getHasDebtDeclinedDecision(LoanRequestDto loanRequest) {
    return new LoanDecisionDto(
        loanRequest.userIdCode(),
        DECLINED,
        loanRequest.periodLength(),
        ZERO
    );
  }

  private static LoanDecisionDto validateAndProceed(LoanDecisionDto loanDecision, int creditModifier) {
    try {
      validateDecision(loanDecision);
    } catch (LoanDecisionMoneyAmountLessThanAllowedValidationException lessMoneyException) {
      /*
      we need to find the period in which it is possible to use at least the min allowed loan amount
      and when the credit score gives the largest allowed amount, so is equal to 1
       */
      final var newPeriod = calculateLoanPeriod(CREDIT_SCORE_FOR_LARGEST_AMOUNT, creditModifier, valueOf(MIN_ALLOWED_LOAN));
      loanDecision = validateAndSetNewPeriod(loanDecision, newPeriod);
    } catch (LoanDecisionMoneyAmountGreaterThanAllowedValidationException moreMoneyException) {
      /*
      we can set the largest allowed money amount here, because the calculated allowed loan is already
      larger than overall acceptable maximum
       */
      loanDecision = loanDecision.withAllowedMoneyAmount(valueOf(MAX_ALLOWED_LOAN));
    } catch (LoanDecisionPeriodLessThanAllowedValidationException shortPeriodException) {
      /*
      we can set the shortest allowed period here, because the calculated allowed period is already
      less than overall acceptable minimum
       */
      loanDecision = loanDecision.withPeriodLength(MIN_PERIOD);
    } catch (LoanDecisionPeriodGreaterThanAllowedValidationException longPeriodException) {
      /*
      we need to find the loan amount with which it is possible to use that within maximum
      allowed period
       */
      final var newAmount = calculateLoanAmount(creditModifier, CREDIT_SCORE_FOR_LARGEST_AMOUNT, MAX_PERIOD);
      loanDecision = validateAndSetNewLoanAmount(loanDecision, newAmount);
    }
    return loanDecision;
  }

  private static LoanDecisionDto validateAndSetNewPeriod(LoanDecisionDto loanDecision, int newPeriod) {
    try {
      validateLoanPeriodLength(newPeriod);
      loanDecision = loanDecision
          .withAllowedMoneyAmount(valueOf(MIN_ALLOWED_LOAN))
          .withPeriodLength(newPeriod);
    } catch (LoanDecisionValidationException validationException) {
      loanDecision = loanDecision.withDecision(DECLINED);
    }
    return loanDecision;
  }

  private static LoanDecisionDto validateAndSetNewLoanAmount(LoanDecisionDto loanDecision, BigDecimal newAmount) {
    try {
      validateLoanMoneyAmount(newAmount);
      loanDecision = loanDecision
          .withPeriodLength(MAX_PERIOD)
          .withAllowedMoneyAmount(newAmount);
    } catch (LoanDecisionValidationException validationException) {
      loanDecision = loanDecision.withDecision(DECLINED);
    }
    return loanDecision;
  }

  private static Optional<LoanDecisionDto> getLoanDecision(LoanRequestDto loanRequest, Integer creditModifier, double creditScore) {
    // if score is equal to 1, then it means that the largest allowed loan amount is chosen
    if (creditScore == CREDIT_SCORE_FOR_LARGEST_AMOUNT) {
      /*
      the best case to have - the largest possible loan amount was requested and is ok
      according to conditions, can simply proceed
       */
      log.info("Loan request with requested period length {} and money amount {} is approved.", loanRequest.periodLength(), loanRequest.requiredMoneyAmount());
      return of(
          new LoanDecisionDto(
              loanRequest.userIdCode(),
              APPROVED,
              loanRequest.periodLength(),
              loanRequest.requiredMoneyAmount()
          )
      );
    // if score is larger than 1, then it means that larger loan amount can be allowed
    // if score is smaller than 1, then it means that smaller amount should be found
    } else if (creditScore > CREDIT_SCORE_FOR_LARGEST_AMOUNT || creditScore < CREDIT_SCORE_FOR_LARGEST_AMOUNT) {
      /*
      case that needs extra validation, because calculated amount may get out of constraints,
      therefore additional validation step is added here
       */
      log.info("Custom loan amount is being calculated");
      var largestAllowedAmount = calculateLoanAmount(creditModifier, CREDIT_SCORE_FOR_LARGEST_AMOUNT, loanRequest.periodLength());
      var updatedDecision = new LoanDecisionDto(
          loanRequest.userIdCode(),
          APPROVED,
          loanRequest.periodLength(),
          largestAllowedAmount
      );
      return of(validateAndProceed(updatedDecision, creditModifier));
    }
    return empty();
  }
}
