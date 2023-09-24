package inbank.decisionengine.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DecisionEngineRestClientException extends RuntimeException {

  private final String code;

  public DecisionEngineRestClientException(String message, String code) {
    super(message);
    this.code = code;
  }
}
