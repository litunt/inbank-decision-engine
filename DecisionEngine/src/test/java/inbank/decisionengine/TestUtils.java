package inbank.decisionengine;

import static inbank.decisionengine.enums.SegmentType.SEGMENT_FOUR;
import static inbank.decisionengine.enums.SegmentType.SEGMENT_ONE;
import static inbank.decisionengine.enums.SegmentType.SEGMENT_THREE;
import static inbank.decisionengine.enums.SegmentType.SEGMENT_TWO;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.StreamUtils.copyToString;

import com.fasterxml.jackson.databind.ObjectMapper;
import inbank.decisionengine.dto.LoanDecisionDto;
import inbank.decisionengine.dto.loanrequest.LoanCondition;
import inbank.decisionengine.dto.loanrequest.LoanProfile;
import inbank.decisionengine.dto.loanrequest.LoanRequest;
import inbank.decisionengine.enums.LoanDecisionType;
import inbank.decisionengine.enums.SegmentType;
import inbank.decisionengine.model.LoanSegmentation;
import inbank.decisionengine.model.Segment;
import inbank.decisionengine.model.UserLoanProfile;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.lang.NonNull;

public class TestUtils {

  public static UserLoanProfile createUserLoanProfileHasDebt(String idCode) {
    var profile = new UserLoanProfile();
    profile.setHasDebt(TRUE);
    profile.setIdCode(idCode);
    profile.setLoanSegmentation(createLoanSegmentation(SEGMENT_FOUR, 600));
    return profile;
  }

  public static UserLoanProfile createUserLoanProfileSegmentOneCreditModifier100(String idCode) {
    var profile = new UserLoanProfile();
    profile.setHasDebt(FALSE);
    profile.setIdCode(idCode);
    profile.setLoanSegmentation(createLoanSegmentation(SEGMENT_ONE, 100));
    return profile;
  }

  public static UserLoanProfile createUserLoanProfileSegmentTwoCreditModifier300(String idCode) {
    var profile = new UserLoanProfile();
    profile.setHasDebt(FALSE);
    profile.setIdCode(idCode);
    profile.setLoanSegmentation(createLoanSegmentation(SEGMENT_TWO, 300));
    return profile;
  }

  public static UserLoanProfile createUserLoanProfileSegmentThreeCreditModifier1000(String idCode) {
    var profile = new UserLoanProfile();
    profile.setHasDebt(FALSE);
    profile.setIdCode(idCode);
    profile.setLoanSegmentation(createLoanSegmentation(SEGMENT_THREE, 1000));
    return profile;
  }

  public static LoanDecisionDto createExpectedLoanDecision(LoanDecisionType decisionType, BigDecimal amount, Integer period) {
    return new LoanDecisionDto(decisionType, period, amount);
  }

  public static LoanRequest createLoanRequestHasDebt() {
    var condition = new LoanCondition(20, BigDecimal.valueOf(3000));
    var profile = new LoanProfile(TRUE, 100);
    return new LoanRequest(condition, profile);
  }

  public static LoanRequest createLoanRequestCreditModifier100(int period, BigDecimal amount) {
    var condition = new LoanCondition(period, amount);
    var profile = new LoanProfile(FALSE, 100);
    return new LoanRequest(condition, profile);
  }

  public static LoanRequest createLoanRequestCreditModifier300(int period, BigDecimal amount) {
    var condition = new LoanCondition(period, amount);
    var profile = new LoanProfile(FALSE, 300);
    return new LoanRequest(condition, profile);
  }

  public static <T> T testDataJson(String path, ObjectMapper objectMapper, Class<T> clazz)
      throws IOException {
    return objectMapper.readValue(readResourceAsString(path), clazz);
  }

  public static String readResourceAsString(@NonNull String path) throws IOException {
    ClassPathResource resource = new ClassPathResource(path);
    if (!resource.exists()) {
      throw new ResourceNotFoundException(path);
    }
    try (InputStream stream = resource.getInputStream()) {
      return readFromStream(stream);
    }
  }

  private static String readFromStream(InputStream inputStream) throws IOException {
    return copyToString(inputStream, UTF_8);
  }

  private static LoanSegmentation createLoanSegmentation(SegmentType type, int creditModifier) {
    var segmentation = new LoanSegmentation();
    segmentation.setCreditModifier(creditModifier);
    segmentation.setSegment(createSegment(type));
    return segmentation;
  }

  private static Segment createSegment(SegmentType type) {
    var segment = new Segment();
    segment.setSegmentType(type);
    return segment;
  }
}
