import {DecisionTypeEnum} from "./enum/decisionType.enum";

export interface LoanDecision {
  decision: DecisionTypeEnum;
  approvedPeriodLength: number;
  approvedLoanAmount: number;
}
