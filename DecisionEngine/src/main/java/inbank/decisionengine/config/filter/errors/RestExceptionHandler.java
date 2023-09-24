package inbank.decisionengine.config.filter.errors;

import static inbank.decisionengine.config.filter.MdcLoggingFilter.TRANSACTION_ID;
import static inbank.decisionengine.utils.constants.ErrorConstants.ERROR_CLIENT_EXCEPTION;
import static inbank.decisionengine.utils.constants.ErrorConstants.ERROR_CONCURRENCY_FAILURE;
import static inbank.decisionengine.utils.constants.ErrorConstants.ERROR_DATA_INTEGRITY_ERROR;
import static inbank.decisionengine.utils.constants.ErrorConstants.ERROR_DATE_TIME_PARSE_EXCEPTION;
import static inbank.decisionengine.utils.constants.ErrorConstants.ERROR_EMPTY_RESULT_DATA_ACCESS;
import static inbank.decisionengine.utils.constants.ErrorConstants.ERROR_INTERNAL_SERVER_ERROR;
import static inbank.decisionengine.utils.constants.ErrorConstants.ERROR_REST_CLIENT_EXCEPTION;
import static inbank.decisionengine.utils.constants.ErrorConstants.ERROR_TIMEOUT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.GATEWAY_TIMEOUT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import inbank.decisionengine.exceptions.DecisionEngineRestClientException;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.MDC;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.NativeWebRequest;

@Slf4j
@ControllerAdvice
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

  private static final String MESSAGE_KEY = "message";

  @ExceptionHandler({
      DecisionEngineRestClientException.class,
      RestClientException.class,
      ResourceAccessException.class,
      HttpClientErrorException.class
  })
  public ResponseEntity<Object> restClientException(Exception ex) {
    log.error(ex.getLocalizedMessage(), ex);
    var restError = new RestError(BAD_REQUEST.value(), "External Service is unavailable or responded with error");
    if (ex instanceof DecisionEngineRestClientException) {
      restError.setErrorCode(((DecisionEngineRestClientException) ex).getCode() != null ? ((DecisionEngineRestClientException) ex).getCode() : ERROR_REST_CLIENT_EXCEPTION);
    } else {
      restError.setErrorCode(ERROR_CLIENT_EXCEPTION);
    }
    restError.setMoreInfo(ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
    restError.setTransactionId(MDC.get(TRANSACTION_ID));
    return new ResponseEntity<>(restError, BAD_REQUEST);
  }

  @ExceptionHandler(TimeoutException.class)
  public ResponseEntity<Object> timeoutException(TimeoutException ex) {
    log.error("TimeoutException", ex);
    var error = new RestError(GATEWAY_TIMEOUT.value(), ex.getMessage());
    error.setErrorCode(ERROR_TIMEOUT);
    return new ResponseEntity<>(error, GATEWAY_TIMEOUT);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Object> dataIntegrityViolationException(DataIntegrityViolationException ex) {
    log.error("DataIntegrityViolationException", ex);
    var error = new RestError(BAD_REQUEST.value(), "Could not process request. Data integrity error");
    error.setErrorCode(ERROR_DATA_INTEGRITY_ERROR);
    error.setMoreInfo(extractDetailedInfo(ex).orElse(null));
    return new ResponseEntity<>(error, BAD_REQUEST);
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  public ResponseEntity<Object> emptyResultDataAccessException(EmptyResultDataAccessException ex) {
    log.error("EmptyResultDataAccessException", ex);
    var error = new RestError(BAD_REQUEST.value(), "Could not process request. Empty result data");
    error.setErrorCode(ERROR_EMPTY_RESULT_DATA_ACCESS);
    error.setMoreInfo(extractDetailedInfo(ex).orElse(null));
    return new ResponseEntity<>(error, BAD_REQUEST);
  }

  @ExceptionHandler(DateTimeParseException.class)
  public ResponseEntity<Object> dateTimeParseException(DateTimeParseException ex) {
    log.error("DateTimeParseException", ex);
    var restError = new RestError(UNPROCESSABLE_ENTITY.value(), "Invalid date value.");
    restError.setErrorCode(ERROR_DATE_TIME_PARSE_EXCEPTION);
    restError.setMoreInfo(extractDetailedInfo(ex).orElse(null));
    return new ResponseEntity<>(restError, UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> internalException(Exception ex) {
    log.error("Internal error", ex);
    var error = new RestError(INTERNAL_SERVER_ERROR.value(), "Internal server error.");
    error.setErrorCode(ERROR_INTERNAL_SERVER_ERROR);
    return new ResponseEntity<>(error, INTERNAL_SERVER_ERROR);
  }

  private Optional<String> extractDetailedInfo(Exception ex) {
    if (ex.getCause() == null) return Optional.of(ex.getMessage());
    return Optional.of(((ConstraintViolationException) ex.getCause()).getSQLException().getMessage());
  }

}
