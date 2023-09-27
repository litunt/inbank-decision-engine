import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class AppErrorService {

  private currentTime: number = new Date().getTime();

  log(error: HttpErrorResponse): void {
    console.error(this.addContextInfo(error));
  }

  addContextInfo(error: HttpErrorResponse): ErrorContextInfo {
    return {
      name: error.name,
      appId: 'ENGINE',
      time: this.currentTime,
      id: `'ENGINE'-${this.currentTime}`,
      status: error.status,
      message: error.message || error.toString(),
      moreInfo: error.error.moreInfo,
      error: error.error
    };
  }
}

interface ErrorContextInfo {
  name: string,
  appId: string,
  time: number,
  id: string,
  status: number,
  message: string,
  moreInfo: string,
  error: HttpErrorResponse
}
