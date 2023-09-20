package inbank.decisionengine.config;

import static inbank.decisionengine.utils.DateUtil.toLocalDateTimeWithSeconds;
import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;
import static java.util.Optional.ofNullable;
import static org.springframework.boot.actuate.health.Health.down;
import static org.springframework.boot.actuate.health.Health.up;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BuildCheck implements HealthIndicator {

  private final BuildProperties buildProperties;
  private final Environment env;

  @Override
  public Health health() {
    try {
      return up().withDetails(Map.of(
          "application", buildProperties.getName(),
          "version", buildProperties.getVersion(),
          "time", toLocalDateTimeWithSeconds(ofInstant(buildProperties.getTime(), systemDefault())),
          "currentTimestamp", toLocalDateTimeWithSeconds(now()),
          "env", ofNullable(env.getProperty("spring.profiles.active")).orElse("environment is not set!"
          ))).build();
    } catch (Exception e) {
      return down().withDetail("Error message", e.getMessage()).build();
    }
  }
}
