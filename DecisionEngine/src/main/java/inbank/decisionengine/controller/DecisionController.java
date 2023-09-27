package inbank.decisionengine.controller;

import static org.springframework.http.HttpStatus.OK;

import inbank.decisionengine.dto.LoanDecisionDto;
import inbank.decisionengine.dto.LoanUserRequestDto;
import inbank.decisionengine.service.LoanEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loan-decision")
public class DecisionController {

  private final LoanEvaluationService loanEvaluationService;

  @PostMapping
  @ResponseStatus(OK)
  public ResponseEntity<LoanDecisionDto> loanDecision(@RequestBody LoanUserRequestDto loanRequest) {
    return ResponseEntity.ok(loanEvaluationService.getLoanDecision(loanRequest));
  }

}
