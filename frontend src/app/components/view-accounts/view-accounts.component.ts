import { Component, OnInit } from '@angular/core';
import { AccountService } from 'src/app/services/account.service';
import { Account } from 'src/app/models/account';

@Component({
  selector: 'app-view-accounts',
  templateUrl: './view-accounts.component.html',
  styleUrls: ['./view-accounts.component.css']
})
export class ViewAccountsComponent implements OnInit {
  accounts: Account[] = [];
  searchId: string = '';

  constructor(private accountService: AccountService) {}

  ngOnInit(): void {
    this.loadAccounts();
  }

  loadAccounts(): void {
    this.accountService.getAllAccounts().subscribe(data => {
      this.accounts = data;
    });
  }

  deleteAccount(accountId: number): void {
    if (confirm('Are you sure to delete this account?')) {
      this.accountService.deleteAccount(accountId).subscribe(() => {
        this.accounts = this.accounts.filter(acc => acc.accountId !== accountId);
      });
    }
  }

  get filteredAccounts(): Account[] {
    if (!this.searchId) return this.accounts;
    return this.accounts.filter(a =>
      a.accountId?.toString().includes(this.searchId)
    );
  }
}
