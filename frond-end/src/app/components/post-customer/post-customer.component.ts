import { Component } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { CustomerService } from "src/app/services/customer.service";

@Component({
  selector: "app-post-customer",
  templateUrl: "./post-customer.component.html",
  styleUrls: ["./post-customer.component.css"],
})
export class PostCustomerComponent {
  postCustomerForm: FormGroup = new FormGroup({});

  constructor(
    private customerService: CustomerService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit() {
    this.postCustomerForm = this.fb.group({
      name: [null, [Validators.required]],
      email: [null, [Validators.required,Validators.email]],
      phone: [null, [Validators.required]],
    });
  }

  postCustomer() {
    console.log(this.postCustomerForm.value);
    this.customerService
      .postCustomer(this.postCustomerForm.value)
      .subscribe((res) => {
        console.log(res);
        this.router.navigateByUrl("");
      });
  }
}
