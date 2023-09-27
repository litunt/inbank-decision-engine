import {Injectable, Injector} from "@angular/core";
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {LoaderService} from "../loader/loader.service";
import {Router} from "@angular/router";
import {TranslateService} from "@ngx-translate/core";
import {AppErrorService} from "./app-error.service";
import {NotificationService} from "../notification.service";

@Injectable()
export class AppHttpInterceptor implements HttpInterceptor {

  constructor(private router: Router,
              private loaderService: LoaderService,
              private injector: Injector,
              private notificationService: NotificationService) {
  }

  intercept(req: HttpRequest<Response>, next: HttpHandler): Observable<HttpEvent<Response>> {
    return next.handle(req).pipe(catchError((error) => {
      const errorsService = this.injector.get(AppErrorService);
      if (error instanceof HttpErrorResponse) {
        // Server error happened
        this.notificationService.addErrorMessage(AppHttpInterceptor.getErrorInfo(error))
        this.loaderService.setLoading(false);
        errorsService.log(error);
      }
      return throwError(() => error);
    }));
  }

  private static getErrorInfo(error: HttpErrorResponse): string {
    return error.error.moreInfo || error.error.message || error.error.errorCode;
  }
}
