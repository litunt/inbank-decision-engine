import {Component} from '@angular/core';
import {ConfigService} from "../../_services/config.service";
import {Config} from "../../_models/config";
import {BehaviorSubject, Subject, tap} from "rxjs";
import {FormControl} from "@angular/forms";
import {LoaderService} from "../../_services/loader/loader.service";
import {DecisionService} from "../../_services/decision.service";
import {LoanRequest} from "../../_models/loanRequest";
import {LoanDecision} from "../../_models/loanDecision";

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
})
export class MainPageComponent {

  config!: Config;

  moneyCtrl: FormControl = new FormControl<number>(0);
  monthCtrl: FormControl = new FormControl<number>(0);
  idCodeCtrl: FormControl = new FormControl<string>("");

  displayDecisionModal: boolean = false;
  decision?: LoanDecision;

  constructor(private configService: ConfigService,
              private loaderService: LoaderService,
              private decisionService: DecisionService) {
    this.config = this.configService.getConfig();
    this.initControls();

  }

  submit(status: boolean): void {
    if (status) {
      this.loaderService.setLoading(true);
      this.decisionService.sendLoanRequest(this.createLoanRequest()).pipe(
        tap((decision: LoanDecision) => {
          this.loaderService.setLoading(false);
          this.displayDecisionModal = true;
          this.decision = decision;
        })
      ).subscribe();
    }
  }

  private initControls(): void {
    this.moneyCtrl?.setValue(this.config.minMoneyAmount);
    this.monthCtrl?.setValue(this.config.minMonthsNumber);
  }

  private createLoanRequest(): LoanRequest {
    return {
      userIdCode: this.idCodeCtrl.value,
      periodLength: this.monthCtrl.value,
      requiredMoneyAmount: this.moneyCtrl.value
    }
  }
}
