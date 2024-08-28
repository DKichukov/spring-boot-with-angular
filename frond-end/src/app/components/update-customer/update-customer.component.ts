import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { CustomerService } from "src/app/services/customer.service";

@Component({
  selector: "app-update-customer",
  templateUrl: "./update-customer.component.html",
  styleUrls: ["./update-customer.component.css"],
})
export class UpdateCustomerComponent implements OnInit {
  id: number = this.activatedRoute.snapshot.params["id"];
  updateCustomerForm: FormGroup = new FormGroup({});

  constructor(
    private activatedRoute: ActivatedRoute,
    private customerService: CustomerService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.updateCustomerForm = this.fb.group({
      name: [null, [Validators.required]],
      email: [null, [Validators.required, Validators.email]],
      phone: [null, [Validators.required]],
    });

    this.getCustomerById();
  }

  getCustomerById() {
    this.customerService.getCustomerById(this.id).subscribe((data) => {
      console.log(data);
      this.updateCustomerForm.patchValue(data);
    });
  }

  updateCustomer() {
    this.customerService
      .updateCustomer(this.id, this.updateCustomerForm.value)
      .subscribe((data) => {
        console.log(data);
        if(this.id !== null){
          this.router.navigate([""]);
        }
      });
  } 
}
