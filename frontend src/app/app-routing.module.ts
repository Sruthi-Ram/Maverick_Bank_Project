import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrationComponent } from './components/registration/registration.component';
import { CustomerDashboardComponent } from './components/customer-dashboard/customer-dashboard.component';
import { LoginComponent } from './components/login/login.component';
import { CustomerRegisterComponent } from './components/customer-register/customer-register.component';
import { CustomerViewProfileComponent } from './components/customer-view-profile/customer-view-profile.component';
import { CustomerUpdateComponent } from './components/customer-update/customer-update.component';
import { OpenAccountComponent } from './components/open-account/open-account.component';
import { MyAccountsComponent } from './components/my-accounts/my-accounts.component';
import { AccountDetailsComponent } from './components/account-details/account-details.component';
import { TransactionsComponent } from './components/transactions/transactions.component';
import { ViewLoansComponent } from './components/view-loans/view-loans.component';
import { ApplyLoanComponent } from './components/apply-loan/apply-loan.component';
import { MyLoansComponent } from './components/my-loans/my-loans.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { ManageUsersComponent } from './components/manage-users/manage-users.component';
import { EmployeeDashboardComponent } from './components/employee-dashboard/employee-dashboard.component';
import { CustomerListComponent } from './components/customer-list/customer-list.component';
import { TransactionViewComponent } from './components/transaction-view/transaction-view.component';
import { ViewAccountsComponent } from './components/view-accounts/view-accounts.component';
import { LoanReviewComponent } from './components/loan-review/loan-review.component';


const routes: Routes = [{ path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'register', component: RegistrationComponent },
  { path: 'login', component: LoginComponent },
  {
    path: 'customer-dashboard', 
    component: CustomerDashboardComponent,
    children: [
      { path: 'customer-register', component: CustomerRegisterComponent },
      { path: 'customer-view-profile/:userId', component: CustomerViewProfileComponent },
      { path: 'customer-update/:customerId', component: CustomerUpdateComponent },
      { path: 'open-account', component: OpenAccountComponent },
      { path: 'my-accounts/:customerId', component: MyAccountsComponent },
      { path: 'account-details/:accountId',component: AccountDetailsComponent},
      { path: 'transactions', component: TransactionsComponent },
      { path: 'view-loans', component: ViewLoansComponent },
      { path: 'apply-loan', component: ApplyLoanComponent },
      { path: 'my-loans/:customerId', component: MyLoansComponent }
    ] 
  },
  {
    path: 'employee-dashboard',
    component: EmployeeDashboardComponent,
    children: [
      { path: 'view-customers', component: CustomerListComponent },
      { path: 'view-transactions', component: TransactionViewComponent },
      { path: 'view-accounts', component: ViewAccountsComponent },
      { path: 'loan-review', component: LoanReviewComponent }

    ]
  },
  { 
    path: 'admin-dashboard', 
    component: AdminDashboardComponent,
    children:[
    { path: 'manage-users', component: ManageUsersComponent }
  ] 
},
  { path: '**', redirectTo: 'login' }
  ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
