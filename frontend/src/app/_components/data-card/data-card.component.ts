import {Component, EventEmitter, Input, Output, TemplateRef} from '@angular/core';

@Component({
  selector: 'app-data-card',
  templateUrl: './data-card.component.html',
})
export class DataCardComponent {

  @Input() body!: TemplateRef<Element>;
  @Input() title!: string;
  @Input() actionLabel!: string;
  @Output() cardActionEmitter: EventEmitter<boolean> = new EventEmitter<boolean>();

  send(): void {
    this.cardActionEmitter.emit(true);
  }
}
