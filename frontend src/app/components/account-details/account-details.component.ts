import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Transaction } from '../../models/transaction';
import { TransactionService } from '../../services/transaction.service';

@Component({
  selector: 'app-account-details',
  templateUrl: './account-details.component.html',
  styleUrls: ['./account-details.component.css']
})
export class AccountDetailsComponent implements OnInit {

  accountId: string = '';
  transactions: Transaction[] = [];
  error: string = '';
  loading: boolean = false;
  

  constructor(
    private route: ActivatedRoute,
    private transactionService: TransactionService
  ) {}

  ngOnInit(): void {
    this.accountId = this.route.snapshot.paramMap.get('accountId') || '';

    if (this.accountId) {
      this.fetchTransactions();
    } else {
      this.error = 'Invalid Account ID';
    }
  }

  fetchTransactions(): void {
    this.loading = true;
    this.transactionService.getTransactionsByAccountId(+this.accountId).subscribe({
      next: (data) => {
        this.transactions = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load transactions.';
        this.loading = false;
      }
    });
  }
}
