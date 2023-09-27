import {Injectable} from "@angular/core";
import {Config} from "../_models/config";

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  public getConfig(): Config {
    return {
      maxMoneyAmount: 10000,
      minMoneyAmount: 2000,
      maxMonthsNumber: 60,
      minMonthsNumber: 12
    }
  }

}
