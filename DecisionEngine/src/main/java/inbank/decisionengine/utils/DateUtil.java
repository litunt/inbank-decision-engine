package inbank.decisionengine.utils;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = PRIVATE)
public final class DateUtil {

  public static final DateTimeFormatter dd_MM_yyyy_HH_mm_ss = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

  public static String toLocalDateTimeWithSeconds(LocalDateTime localDateTime) {
    return localDateTime.format(dd_MM_yyyy_HH_mm_ss);
  }

}
