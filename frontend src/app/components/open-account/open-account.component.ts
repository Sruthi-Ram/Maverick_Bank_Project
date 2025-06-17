import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AccountService } from '../../services/account.service';
import { AuthService } from '../../services/auth.service';
import { CustomerService } from '../../services/customer.service';
import { BankBranchService } from '../../services/bank-branch.service';  // Import BankBranchService
import { Account } from '../../models/account';
import { BankBranch } from '../../models/bank-branch'; // Interface for branches

@Component({
  selector: 'app-open-account',
  templateUrl: './open-account.component.html',
  styleUrls: ['./open-account.component.css']
})
export class OpenAccountComponent implements OnInit {
  account: Account = {
    customerId: null,
    branchId: 0,
    accountType: '',
    balance: 0,
    ifscCode: ''
  };

  bankBranches: BankBranch[] = [];  // Store fetched branches here

  success = false;
  error = '';

  constructor(
    private accountService: AccountService,
    private authService: AuthService,
    private customerService: CustomerService,
    private bankBranchService: BankBranchService  // Inject BankBranchService
  ) {}

  ngOnInit(): void {
    // Get current logged-in user and fetch their customer ID
    const user = this.authService.getCurrentUser();
    if (user?.userId) {
      this.customerService.getCustomerByUserId(user.userId).subscribe({
        next: (res) => {
          this.account.customerId = res?.customerId ?? null;
        },
        error: () => {
          this.error = 'Customer not found';
        }
      });
    }

    // Fetch all bank branches
    this.bankBranchService.getAllBankBranches().subscribe({
      next: (branches) => {
        this.bankBranches = branches;
      },
      error: () => {
        this.error = 'Failed to load bank branches';
      }
    });
  }

  onSubmit(form: NgForm) {
    if (form.valid) {
      this.accountService.openAccount(this.account).subscribe({
        next: () => {
          this.success = true;
          this.error = '';
          form.resetForm({
            branchId: 0,
            accountType: '',
            balance: 0,
            ifscCode: '',
            customerId: this.account.customerId
          });
        },
        error: () => {
          this.success = false;
          this.error = 'Account creation failed';
        }
      });
    }
  }
}
