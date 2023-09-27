import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormControl} from "@angular/forms";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-id-code-selector',
  templateUrl: './id-code-selector.component.html',
  styleUrls: ['./id-code-selector.component.css']
})
export class IdCodeSelectorComponent {

  ctrl!: FormControl;
  idCodes: string[] = [
    '49002010965',
    '49002010976',
    '49002010987',
    '49002010998'
  ]

  @Input() placeholder: string = '';
  @Input() header: string = '';

  @Output() idCodeCtrlChange: EventEmitter<FormControl> = new EventEmitter<FormControl>();
  @Input() set idCodeCtrl(ctrl: FormControl) {
    this.ctrl = ctrl;
  }

  private users: UserProfile[] = [
    { idCode: '49002010965', segment: "4", creditModifier: 50, hasDebt: true},
    { idCode: '49002010976', segment: "1", creditModifier: 100, hasDebt: false},
    { idCode: '49002010987', segment: "2", creditModifier: 300, hasDebt: false},
    { idCode: '49002010998', segment: "3", creditModifier: 1000, hasDebt: false},
  ]

  get getPlaceholderText(): string {
    return this.translate.instant(this.placeholder);
  }

  get getHeaderData(): string {
    const usr: UserProfile = this.users.find((u: UserProfile) => u.idCode === this.ctrl.value)!;
    if (!usr) {
      return '';
    }
    if (usr.hasDebt) {
      return `${usr.idCode} (has debt)`;
    }
    return `${usr.idCode} (segment: ${usr.segment}, credit modifier: ${usr.creditModifier})`;
  }

  constructor(private translate: TranslateService) {
  }

}

interface UserProfile {
  idCode: string;
  segment: string;
  creditModifier: number;
  hasDebt: boolean;
}
