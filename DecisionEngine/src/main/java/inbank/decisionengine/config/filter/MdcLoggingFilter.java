package inbank.decisionengine.config.filter;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.filter.OncePerRequestFilter;

@AllArgsConstructor
public class MdcLoggingFilter extends OncePerRequestFilter {

  public static final String PROGRAM_VERSION = "program_version";
  public static final String TRANSACTION_ID = "transaction_id";
  private final BuildProperties buildProperties;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, final FilterChain chain)
      throws IOException, ServletException {
    try {
      String existingTransactionId = request.getHeader(TRANSACTION_ID);
      String transactionId = isNotBlank(existingTransactionId) ? existingTransactionId : randomUUID().toString();
      MDC.put(TRANSACTION_ID, transactionId);
      MDC.put(PROGRAM_VERSION, buildProperties.getVersion());
      response.addHeader(TRANSACTION_ID, transactionId);

      chain.doFilter(request, response);
    } finally {
      MDC.remove(TRANSACTION_ID);
      MDC.remove(PROGRAM_VERSION);
    }
  }
}
