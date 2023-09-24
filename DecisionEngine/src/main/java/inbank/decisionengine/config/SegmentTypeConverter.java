package inbank.decisionengine.config;

import inbank.decisionengine.enums.SegmentType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class SegmentTypeConverter implements AttributeConverter<SegmentType, String> {

  @Override
  public String convertToDatabaseColumn(SegmentType segmentType) {
    if (segmentType == null) {
      return null;
    }
    return segmentType.geType();
  }

  @Override
  public SegmentType convertToEntityAttribute(String code) {
    if (code == null) {
      return null;
    }

    return Stream.of(SegmentType.values())
        .filter(c -> c.geType().equals(code))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
