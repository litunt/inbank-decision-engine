package inbank.decisionengine.enums;

public enum SegmentType {
  SEGMENT_ONE("1"),
  SEGMENT_TWO("2"),
  SEGMENT_THREE("3");

  private final String type;

  SegmentType(String type) {
    this.type = type;
  }

  public String geType() {
    return this.type;
  }
}
