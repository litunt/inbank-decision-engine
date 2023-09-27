import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {LoanRequest} from "../_models/loanRequest";
import {Observable} from "rxjs";
import {LoanDecision} from "../_models/loanDecision";

@Injectable({
  providedIn: 'root'
})
export class DecisionService {

  constructor(private http: HttpClient) {  }

  public sendLoanRequest(loanRequest: LoanRequest): Observable<LoanDecision> {
    return this.http.post<LoanDecision>('/loan-decision', loanRequest);
  }
}
