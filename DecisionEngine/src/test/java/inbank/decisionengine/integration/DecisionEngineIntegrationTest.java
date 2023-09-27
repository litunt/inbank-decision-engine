package inbank.decisionengine.integration;

import static inbank.decisionengine.TestUtils.createExpectedLoanDecision;
import static inbank.decisionengine.TestUtils.createUserLoanProfileHasDebt;
import static inbank.decisionengine.TestUtils.createUserLoanProfileSegmentOneCreditModifier100;
import static inbank.decisionengine.TestUtils.createUserLoanProfileSegmentThreeCreditModifier1000;
import static inbank.decisionengine.TestUtils.createUserLoanProfileSegmentTwoCreditModifier300;
import static inbank.decisionengine.TestUtils.readResourceAsString;
import static inbank.decisionengine.TestUtils.testDataJson;
import static inbank.decisionengine.enums.LoanDecisionType.ADJUSTED;
import static inbank.decisionengine.enums.LoanDecisionType.APPROVED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import inbank.decisionengine.dto.LoanDecisionDto;
import inbank.decisionengine.repository.UserLoanProfileRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.mock.mockito.MockBean;

class DecisionEngineIntegrationTest extends IntegrationTest {

  @MockBean
  UserLoanProfileRepository userLoanProfileRepository;

  @ParameterizedTest
  @CsvSource(value = "LoanRequest_HasDebt.json:response/LoanDecision_DECLINED.json:49002010965", delimiter = ':')
  @DisplayName("Test user has debt, should decline loan request")
  void testUserHasDebtDShouldDeclineLoanRequest(String request, String response, String idCode) throws Exception {
    final var userProfile = Optional.of(createUserLoanProfileHasDebt(idCode));
    when(userLoanProfileRepository.findByIdCode(idCode)).thenReturn(userProfile);
    var actualResponse = performMockAndReturnDecision(request);
    var expectedResponse = testDataJson(response, objectMapper, LoanDecisionDto.class);
    checkAndAssert(actualResponse, expectedResponse);
  }

  @ParameterizedTest
  @CsvSource(value = "LoanRequest_Segment1CreditModifier100.json:49002010976", delimiter = ':')
  @DisplayName("Test segment 1 credit modifier 100 should adjust loan request")
  void testSegment1CreditModifier100ShouldAdjustLoanRequest(String request, String idCode) throws Exception {
    final var userProfile = Optional.of(createUserLoanProfileSegmentOneCreditModifier100(idCode));
    when(userLoanProfileRepository.findByIdCode(idCode)).thenReturn(userProfile);
    var actualResponse = performMockAndReturnDecision(request);
    var expectedResponse = createExpectedLoanDecision(ADJUSTED, new BigDecimal("2000.00"), 20);
    checkAndAssert(actualResponse, expectedResponse);
  }

  @ParameterizedTest
  @CsvSource(value = "LoanRequest_Segment2CreditModifier300.json:49002010987", delimiter = ':')
  @DisplayName("Test segment 2 credit modifier 300 should adjust loan request")
  void testSegment2CreditModifier300ShouldAdjustLoanRequest(String request, String idCode) throws Exception {
    final var userProfile = Optional.of(createUserLoanProfileSegmentTwoCreditModifier300(idCode));
    when(userLoanProfileRepository.findByIdCode(idCode)).thenReturn(userProfile);
    var actualResponse = performMockAndReturnDecision(request);
    var expectedResponse = createExpectedLoanDecision(ADJUSTED, new BigDecimal("3750.00"), 12);
    checkAndAssert(actualResponse, expectedResponse);
  }

  @ParameterizedTest
  @CsvSource(value = "LoanRequest_Segment3CreditModifier1000.json:49002010998", delimiter = ':')
  @DisplayName("Test segment 3 credit modifier 1000 should adjust loan request")
  void testSegment3CreditModifier1000ShouldAdjustLoanRequest(String request, String idCode) throws Exception {
    final var userProfile = Optional.of(createUserLoanProfileSegmentThreeCreditModifier1000(idCode));
    when(userLoanProfileRepository.findByIdCode(idCode)).thenReturn(userProfile);
    var actualResponse = performMockAndReturnDecision(request);
    var expectedResponse = createExpectedLoanDecision(ADJUSTED, new BigDecimal("10000"), 46);
    checkAndAssert(actualResponse, expectedResponse);
  }

  @ParameterizedTest
  @CsvSource(value = "LoanRequest_Segment1CreditModifier100_Approve.json:49002010976", delimiter = ':')
  @DisplayName("Test segment 1 credit modifier 100 should approve loan request")
  void testSegment1CreditModifier100ShouldApproveLoanRequest(String request, String idCode) throws Exception {
    final var userProfile = Optional.of(createUserLoanProfileSegmentOneCreditModifier100(idCode));
    when(userLoanProfileRepository.findByIdCode(idCode)).thenReturn(userProfile);
    var actualResponse = performMockAndReturnDecision(request);
    var expectedResponse = createExpectedLoanDecision(APPROVED, new BigDecimal("2000"), 20);
    checkAndAssert(actualResponse, expectedResponse);
  }

  @ParameterizedTest
  @CsvSource(value = "LoanRequest_Segment2CreditModifier300_Approve.json:49002010987", delimiter = ':')
  @DisplayName("Test segment 2 credit modifier 300 should approve loan request")
  void testSegment2CreditModifier300ShouldApproveLoanRequest(String request, String idCode) throws Exception {
    final var userProfile = Optional.of(createUserLoanProfileSegmentTwoCreditModifier300(idCode));
    when(userLoanProfileRepository.findByIdCode(idCode)).thenReturn(userProfile);
    var actualResponse = performMockAndReturnDecision(request);
    var expectedResponse = createExpectedLoanDecision(APPROVED, new BigDecimal("6000"), 20);
    checkAndAssert(actualResponse, expectedResponse);
  }

  @ParameterizedTest
  @CsvSource(value = "LoanRequest_Segment3CreditModifier1000_BestCase_Still_Adjust.json:49002010998", delimiter = ':')
  @DisplayName("Test segment 3 credit modifier 1000 should approve loan request")
  void testSegment3CreditModifier1000ShouldApproveLoanRequest(String request, String idCode) throws Exception {
    final var userProfile = Optional.of(createUserLoanProfileSegmentThreeCreditModifier1000(idCode));
    when(userLoanProfileRepository.findByIdCode(idCode)).thenReturn(userProfile);
    var actualResponse = performMockAndReturnDecision(request);
    var expectedResponse = createExpectedLoanDecision(ADJUSTED, new BigDecimal("10000"), 12);
    checkAndAssert(actualResponse, expectedResponse);
  }

  @ParameterizedTest
  @CsvSource(value = "LoanRequest_Segment1CreditModifier100_Adjusted_Amount_And_Period.json:49002010976", delimiter = ':')
  @DisplayName("Test segment 1 credit modifier 100 should approve loan request")
  void testSegment1CreditModifier100ShouldAdjustAmountAndPeriodLoanRequest(String request, String idCode) throws Exception {
    final var userProfile = Optional.of(createUserLoanProfileSegmentOneCreditModifier100(idCode));
    when(userLoanProfileRepository.findByIdCode(idCode)).thenReturn(userProfile);
    var actualResponse = performMockAndReturnDecision(request);
    var expectedResponse = createExpectedLoanDecision(ADJUSTED, new BigDecimal("2000"), 20);
    checkAndAssert(actualResponse, expectedResponse);
  }

  private LoanDecisionDto performMockAndReturnDecision(String request) throws Exception {
    var loanRequest = readResourceAsString(request);

    var responseAsString = mockMvc.perform(post(("/loan-decision"))
            .contentType(APPLICATION_JSON)
            .content(loanRequest))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse().getContentAsString();

    return objectMapper.readValue(responseAsString, LoanDecisionDto.class);
  }

  private void checkAndAssert(LoanDecisionDto actualResponse, LoanDecisionDto expectedResponse) {
    assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
  }
}
