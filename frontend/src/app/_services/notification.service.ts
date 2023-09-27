import {Injectable} from "@angular/core";
import {MessageService} from "primeng/api";
import {TranslateService} from "@ngx-translate/core";
import {NotificationType} from "../_models/enum/notificationType";

@Injectable({
    providedIn: 'root'
})
export class NotificationService {

  constructor(private messageService: MessageService,
              private translateService: TranslateService) {}

  addSuccessMessage(message: string, details?: string) {
    this.addMessage(NotificationType.success, message, details);
  }

  addInfoMessage(message: string, details?: string) {
    this.addMessage(NotificationType.info, message, details);
  }

  addErrorMessage(message: string, details?: string) {
    this.addMessage(NotificationType.error, message, details);
  }

  addWarningMessage(message: string, details?: string) {
    this.addMessage(NotificationType.warn, message, details);
  }

  clear() {
    this.messageService.clear();
  }

  private addMessage(level: NotificationType, message: string, details?: string) {
    this.messageService.add(
      {
        key: level + "-toast",
        severity: level,
        summary: this.translateService.instant(message),
        detail: details ? this.translateService.instant(details) : '',
        sticky: true
      });
  }

}
