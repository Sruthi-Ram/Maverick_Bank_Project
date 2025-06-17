import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './interceptors/auth.interceptor';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { HttpClientModule }  from '@angular/common/http';
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


@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
       CustomerDashboardComponent,
       LoginComponent,
       CustomerRegisterComponent,
       CustomerViewProfileComponent,
       CustomerUpdateComponent,
       OpenAccountComponent,
       MyAccountsComponent,
       AccountDetailsComponent,
       TransactionsComponent,
       ViewLoansComponent,
       ApplyLoanComponent,
       MyLoansComponent,
       AdminDashboardComponent,
       ManageUsersComponent,
       EmployeeDashboardComponent,
       CustomerListComponent,
       TransactionViewComponent,
       ViewAccountsComponent,
       LoanReviewComponent,
      
  ],
  imports: [
    BrowserModule,
      FormsModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    CommonModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
