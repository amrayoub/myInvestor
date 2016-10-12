import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

/*
  Stock database provider.
*/
@Injectable()
export class StockDatabase {

  constructor(public http: Http) {
    console.log('Hello StockDatabase Provider');
  }
}
