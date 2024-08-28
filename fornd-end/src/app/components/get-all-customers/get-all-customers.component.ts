import { Component } from "@angular/core";
import { CustomerService } from "src/app/services/customer.service";

@Component({
  selector: "app-get-all-customers",
  templateUrl: "./get-all-customers.component.html",
  styleUrls: ["./get-all-customers.component.css"],
})
export class GetAllCustomersComponent {
  customers: any = [];

  constructor(private customerService: CustomerService) {}

  ngOnInit() {
    this.getAllCustomers();
  }

  getAllCustomers() {
    this.customerService.getAllCustomers().subscribe((res) => {
      this.customers = res;
      console.log(res);
    });
  }
}
