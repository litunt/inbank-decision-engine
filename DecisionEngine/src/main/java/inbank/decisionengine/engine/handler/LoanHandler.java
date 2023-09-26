package inbank.decisionengine.engine.handler;

import inbank.decisionengine.dto.LoanDecisionDto;
import inbank.decisionengine.dto.loanrequest.LoanRequest;

public interface LoanHandler {

  void setNextHandler(LoanHandler handler);
  LoanDecisionDto handle(LoanRequest loanRequest);

}
