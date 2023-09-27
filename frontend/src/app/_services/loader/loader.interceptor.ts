import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoaderService } from './loader.service';
import { catchError, map, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoaderInterceptor implements HttpInterceptor {

  constructor(public loaderService: LoaderService) {
  }

  intercept(req: HttpRequest<Response>, next: HttpHandler): Observable<HttpEvent<Response>> {
    return next.handle(req)
      .pipe(catchError((err) => {
        return throwError(() => err);
      }))
      .pipe(
        map<HttpEvent<Response>, HttpEvent<Response>>((event: HttpEvent<Response>) => {
          return event;
        })
      );
  }
}
