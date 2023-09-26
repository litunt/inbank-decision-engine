import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormControl} from "@angular/forms";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-range-slider',
  templateUrl: './range-slider.component.html',
  styleUrls: ['./range-slider.component.css']
})
export class RangeSliderComponent {

  @Input() max!: number;
  @Input() min!: number;
  @Input() step: number = 1;
  @Input() suffix!: string;
  @Input() header!: string;

  ctrl!: FormControl;

  @Output() sliderCtrlChange: EventEmitter<FormControl> = new EventEmitter<FormControl>();
  @Input() set sliderCtrl(ctrl: FormControl) {
    this.ctrl = ctrl;
  }

  get getHeaderValue(): string {
    return `${this.ctrl && this.ctrl.value || this.min} ${this.translate.instant(this.suffix)}`;
  }
  constructor(private translate: TranslateService) {
  }
}
