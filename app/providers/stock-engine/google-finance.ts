import { Injectable } from '@angular/core';
import { StockEngine } from './stock-engine';

// http://www.google.com/finance/historical?q=KLSE%3ASEM&ei=ua3eV6GkKMyauASU475o&output=csv
@Injectable()
export class GoogleFinance extends StockEngine {
  
}