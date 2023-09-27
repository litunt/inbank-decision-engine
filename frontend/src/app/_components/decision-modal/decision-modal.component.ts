import {Component, EventEmitter, Input, Output} from '@angular/core';
import {LoanDecision} from "../../_models/loanDecision";
import {DecisionTypeEnum} from "../../_models/enum/decisionType.enum";

@Component({
  selector: 'app-decision-modal',
  templateUrl: './decision-modal.component.html',
  styleUrls: ['./decision-modal.component.css']
})
export class DecisionModalComponent {

  @Output() displayModalChange: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Input() set displayModal(value: boolean) {
    this.display = value;
  }
  @Input() decision?: LoanDecision;

  display: boolean = false;

  get getDecisionTitle(): string {
    if (this.decision) {
      switch (this.decision.decision) {
        case DecisionTypeEnum.ADJUSTED:
          return 'Loan application approved, but adjusted';
        case DecisionTypeEnum.APPROVED:
          return 'Loan application approved';
        case DecisionTypeEnum.DECLINED:
          return 'Loan application declined';
        case DecisionTypeEnum.FAILURE:
          return 'Could not process loan application';
        default:
          return ''
      }
    }
    return '';
  }

  closeModal(): void {
    this.display = false;
    this.displayModalChange.emit(false);
  }

}
