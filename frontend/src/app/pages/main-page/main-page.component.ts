import { Component } from '@angular/core';
import {ConfigService} from "../../_services/config.service";
import {Config} from "../../_models/config";
import {tap} from "rxjs";
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
})
export class MainPageComponent {

  config!: Config;
  form: FormGroup = new FormGroup({});

  moneyCtrl: FormControl = new FormControl<number>(0);
  monthCtrl: FormControl = new FormControl<number>(0);

  constructor(private configService: ConfigService) {
    this.configService.getConfig().pipe(
      tap((conf: Config) => {
        this.config = conf;
        this.initControls();
      })
    ).subscribe();

  }

  submit(status: boolean): void {
    console.log(this.form);
  }

  private initControls(): void {
    this.moneyCtrl?.setValue(this.config.minMoneyAmount);
    this.monthCtrl?.setValue(this.config.minMonthsNumber);
    this.form.addControl('money', this.moneyCtrl);
    this.form.addControl('months', this.monthCtrl);
  }
}
