package inbank.decisionengine.controller;

import static org.springframework.http.HttpStatus.OK;

import inbank.decisionengine.model.UserLoanProfile;
import inbank.decisionengine.repository.UserLoanProfileRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loan-decision")
public class DecisionController {

  private final UserLoanProfileRepository userLoanProfileRepository;

  @GetMapping
  @ResponseStatus(OK)
  public ResponseEntity<List<UserLoanProfile>> getProfiles() {
    return ResponseEntity.ok(userLoanProfileRepository.findAll());
  }

}
