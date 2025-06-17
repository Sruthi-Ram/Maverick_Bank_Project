import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoanService } from '../../services/loan.service';
import { Loan } from '../../models/loan';

@Component({
  selector: 'app-view-loans',
  templateUrl: './view-loans.component.html',
  styleUrls: ['./view-loans.component.css']
})
export class ViewLoansComponent implements OnInit {
  loans: Loan[] = [];

  constructor(private loanService: LoanService, private router: Router) {}

  ngOnInit(): void {
    this.loanService.getAllLoans().subscribe(data => this.loans = data);
  }

  onApplyLoan(loan: Loan): void {
    this.router.navigate(['/customer-dashboard/apply-loan'], {
      queryParams: { loanId: loan.loanId }
    });
  }
}
