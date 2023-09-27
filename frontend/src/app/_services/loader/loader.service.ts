import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class LoaderService {

  private _loading: boolean = false;

  setLoading(value: boolean) {
    this._loading = value;
  }

  get isLoading() {
    return this._loading;
  }
}
