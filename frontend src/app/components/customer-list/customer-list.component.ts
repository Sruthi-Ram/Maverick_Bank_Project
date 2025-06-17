import { Component, OnInit } from '@angular/core';
import { CustomerService } from 'src/app/services/customer.service';
import { Customer } from 'src/app/models/customer';

@Component({
  selector: 'app-customer-list',
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.css']
})
export class CustomerListComponent implements OnInit {
  customers: Customer[] = [];
  filteredCustomers: Customer[] = [];
  searchId: string = '';

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.customerService.getAllCustomers().subscribe(data => {
      this.customers = data;
      this.filteredCustomers = data;
    });
  }

  filterCustomers(): void {
    if (this.searchId.trim() === '') {
      this.filteredCustomers = this.customers;
    } else {
      const id = parseInt(this.searchId, 10);
      this.filteredCustomers = this.customers.filter(c => c.customerId === id);
    }
  }
}
