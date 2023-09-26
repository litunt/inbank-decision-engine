import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {Config} from "../_models/config";

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  constructor(private http: HttpClient) {}

  public getConfig(): Observable<Config> {
    // return this.http.get<Config>('/api/config');
    return of({
      maxMoneyAmount: 10000,
      minMoneyAmount: 2000,
      maxMonthsNumber: 60,
      minMonthsNumber: 12
    })
  }

}
