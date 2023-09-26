package inbank.decisionengine.service;

import static inbank.decisionengine.utils.constants.ErrorConstants.ERROR_USER_PROFILE_NOT_FOUND;
import static inbank.decisionengine.validation.DecisionValidator.validateRequest;
import static java.lang.String.format;

import inbank.decisionengine.dto.LoanDecisionDto;
import inbank.decisionengine.dto.loanrequest.LoanCondition;
import inbank.decisionengine.dto.loanrequest.LoanProfile;
import inbank.decisionengine.dto.loanrequest.LoanRequest;
import inbank.decisionengine.dto.LoanUserRequestDto;
import inbank.decisionengine.engine.handler.LoanHandler;
import inbank.decisionengine.exceptions.DecisionEngineRestClientException;
import inbank.decisionengine.repository.UserLoanProfileRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanEvaluationService {
  private final UserLoanProfileRepository userLoanProfileRepository;
  private final LoanHandler hasDebtLoanHandler;
  private final LoanHandler initialDecisionLoanHandler;
  private final LoanHandler maxLoanAmountLoanHandler;
  private final LoanHandler minLoanAmountLoanHandler;
  private final LoanHandler maxPeriodLoanHandler;
  private final LoanHandler minPeriodLoanHandler;
  private final LoanHandler completeRejectionLoanHandler; // TODO rename


  @PostConstruct
  public void initHandlers() {
    hasDebtLoanHandler.setNextHandler(initialDecisionLoanHandler);
    initialDecisionLoanHandler.setNextHandler(maxLoanAmountLoanHandler);
    maxLoanAmountLoanHandler.setNextHandler(minLoanAmountLoanHandler);
    minLoanAmountLoanHandler.setNextHandler(minPeriodLoanHandler);
    minPeriodLoanHandler.setNextHandler(maxPeriodLoanHandler);
    maxPeriodLoanHandler.setNextHandler(completeRejectionLoanHandler);
  }

  public LoanDecisionDto getLoanDecision(LoanUserRequestDto userLoanRequest) {
    var userLoanProfile = userLoanProfileRepository.findByIdCode(userLoanRequest.userIdCode());
    if (userLoanProfile.isPresent()) {
      validateRequest(userLoanRequest);
      final var userProfile = userLoanProfile.get();
      var loanRequest = new LoanRequest(
          new LoanCondition(userLoanRequest.periodLength(), userLoanRequest.requiredMoneyAmount()),
          new LoanProfile(userProfile.getHasDebt(), userProfile.getLoanSegmentation().getCreditModifier())
      );
      return hasDebtLoanHandler.handle(loanRequest);
    } else {
      throw new DecisionEngineRestClientException(format("User profile for user id code %s was not found.", userLoanRequest.userIdCode()), ERROR_USER_PROFILE_NOT_FOUND);
    }
  }
}

