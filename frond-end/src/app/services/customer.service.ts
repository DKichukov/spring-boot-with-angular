import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

const BASIC_URL = ["http://localhost:8090"];

@Injectable({
  providedIn: "root",
})
export class CustomerService {

  constructor(private http: HttpClient) {
  }

  postCustomer(customer: any): Observable<any> {
    return this.http.post(BASIC_URL + "/api/customer", customer);
  }

  getAllCustomers(): Observable<any> {
    return this.http.get(BASIC_URL + "/api/customers");
  }

  getCustomerById(customerId: number): Observable<any> {
    return this.http.get(BASIC_URL + "/api/customer/" + customerId);
  }
}
