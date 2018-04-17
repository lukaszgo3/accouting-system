import { Injectable } from '@angular/core';
import { Company } from './company';
import { COMPANIES } from './mock-companies';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class CompanyService {
  private apiUrl = 'http://localhost:8080/v2/company';  // URL to web api

  getCompanies(): Observable<Company[]> {
    this.messageService.add('CompanyService: fetched companies');
    return this.http.get<Company[]>(this.apiUrl);
  }

  constructor(private messageService: MessageService,
    private http: HttpClient) { }


  /** Log a HeroService message with the MessageService */
  private log(message: string) {
    this.messageService.add('CompanyService: ' + message);
  }
}
