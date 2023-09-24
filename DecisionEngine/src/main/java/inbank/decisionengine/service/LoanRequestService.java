package inbank.decisionengine.service;

import static inbank.decisionengine.decisionengine.DecisionEngine.calculateDecision;
import static inbank.decisionengine.utils.constants.ErrorConstants.ERROR_USER_PROFILE_NOT_FOUND;
import static inbank.decisionengine.validation.DecisionValidator.validateRequest;
import static java.lang.String.format;

import inbank.decisionengine.dto.LoanDecisionDto;
import inbank.decisionengine.dto.LoanRequestDto;
import inbank.decisionengine.exceptions.DecisionEngineRestClientException;
import inbank.decisionengine.model.UserLoanProfile;
import inbank.decisionengine.repository.UserLoanProfileRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanRequestService {
  private final UserLoanProfileRepository userLoanProfileRepository;

  public LoanDecisionDto getLoanDecision(LoanRequestDto loanRequest) {
    var userLoanProfile = userLoanProfileRepository.findByIdCode(loanRequest.userIdCode());
    if (userLoanProfile.isPresent()) {
      validateRequest(loanRequest);
      return calculateDecision(loanRequest, userLoanProfile.get());
    } else {
      throw new DecisionEngineRestClientException(format("User profile for user id code %s was not found.", loanRequest.userIdCode()), ERROR_USER_PROFILE_NOT_FOUND);
    }
  }
}

