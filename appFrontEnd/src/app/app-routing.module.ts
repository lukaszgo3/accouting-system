import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyComponent } from './company/company.component';
import { EditCompanyComponent } from './edit-company/edit-company.component';
import { AddCompanyComponent } from './add-company/add-company.component';
import { CompanyDetailComponent } from './company-detail/company-detail.component';
import { CompanySearchComponent } from './company-search/company-search.component';

const routes: Routes = [
  { path: 'addCompany', component: AddCompanyComponent },
  { path: 'companies', component: CompanyComponent },
  { path: 'companyEdit/:id', component: EditCompanyComponent },
  { path: '', redirectTo: '/companies', pathMatch: 'full' },
  { path: 'detail/:id', component: CompanyDetailComponent },
  { path: 'searchCompany', component: CompanySearchComponent }
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }