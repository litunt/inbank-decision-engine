package inbank.decisionengine.config.filter.errors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RestError {

    private int status = -1;
    private String message;
    private String errorCode;
    private String transactionId;
    private Object moreInfo;

    public RestError(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
