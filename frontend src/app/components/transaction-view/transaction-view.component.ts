import { Component, OnInit } from '@angular/core';
import { TransactionService } from 'src/app/services/transaction.service';
import { Transaction } from 'src/app/models/transaction';

@Component({
  selector: 'app-transaction-view',
  templateUrl: './transaction-view.component.html',
  styleUrls: ['./transaction-view.component.css']
})
export class TransactionViewComponent implements OnInit {
  allTransactions: Transaction[] = [];
  filteredTransactions: Transaction[] = [];
  accountIdSearch: string = '';

  constructor(private transactionService: TransactionService) {}

  ngOnInit(): void {
    this.loadAllTransactions();
  }

  loadAllTransactions(): void {
    this.transactionService.getAllTransactions().subscribe(data => {
      this.allTransactions = data;
      this.filteredTransactions = data;
    });
  }

  filterByAccountId(): void {
    if (this.accountIdSearch.trim() === '') {
      this.filteredTransactions = this.allTransactions;
    } else {
      const accountId = parseInt(this.accountIdSearch, 10);
      this.filteredTransactions = this.allTransactions.filter(
        tx => tx.accountId === accountId
      );
    }
  }
}
