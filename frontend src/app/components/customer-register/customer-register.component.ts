import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { NgForm } from '@angular/forms';
import { Customer } from '../../models/customer';

@Component({
  selector: 'app-customer-register',
  templateUrl: './customer-register.component.html',
  styleUrls: ['./customer-register.component.css']
})
export class CustomerRegisterComponent implements OnInit {
  customer: Customer = {
    userId:0,
    name: '',
    gender: '',
    contactNumber: '',
    address: '',
    dateOfBirth: '',
    aadharNumber: '',
    panNumber: ''
  };

  storedUserId: number = 0;
  
  constructor(private route: ActivatedRoute, private customerService: CustomerService) {}

ngOnInit(): void {
  const currentUserJson = localStorage.getItem('currentUser');
  if (currentUserJson) {
    const user = JSON.parse(currentUserJson);
    this.storedUserId = user.userId;
    this.customer.userId = user.userId;
    console.log("userId set to:", user.userId);
  }
}



onSubmit(form: NgForm) {
    if (form.valid && this.customer.userId === this.storedUserId) {
      this.customerService.registerCustomer(this.customer).subscribe({
        next: () => {
          alert('Registration successful!');
          form.resetForm();
        },
        error: (err) => {
          alert('Registration failed. Try again.');
          console.error(err);
        }
      });
    } else {
      alert('Entered User ID does not match your login ID.');
    }
  }
}


