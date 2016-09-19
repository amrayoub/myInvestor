import { Injectable } from '@angular/core';
import { Storage, SqlStorage} from 'ionic-angular';
import { Http } from '@angular/http';

import { StockMarket, Stock } from '../../providers/stock-service/stock-model';

import 'rxjs/add/operator/map';

const DATABASE_NAME = 'myInvestor';
const CREATE_STOCK_MARKET_TABLE = 'CREATE TABLE IF NOT EXISTS market (id INTEGER PRIMARY KEY AUTOINCREMENT, symbol TEXT, name TEXT, created DATETIME DEFAULT CURRENT_TIMESTAMP)';
const CREATE_STOCK_TABLE = 'CREATE TABLE IF NOT EXISTS stock (id INTEGER PRIMARY KEY AUTOINCREMENT, symbol TEXT, name TEXT, market_id INTEGER, created DATETIME DEFAULT CURRENT_TIMESTAMP)';


// Global stocks
export class StockConfig {
  // FTSE Bursa Malaysia
  static KLSE_EXCHANGE_SYMBOL: string = 'KLSE';
  static KLSE_EXCHANGE_NAME:string  = 'FTSE Bursa Malaysia';

}

/*
  Database access layer for the stock database.
*/
@Injectable()
export class StockService {

  // SqlStorage instance
  storage: Storage = null;

  // Constructor
  constructor(public http: Http) {
    this.storage = new Storage(SqlStorage, { name: DATABASE_NAME });

    // For 1st time setup, create the necessary tables and populate the data
    this.createMarketTable();

  }

  // Retrieve all support markets
  public getMarkets() {
    return this.storage.query('SELECT * FROM market');
  }

  // Get market by symbol
  public getMarket(symbol: string) {
    return this.storage.query('SELECT * FROM market WHERE symbol = ?', [symbol]);
  }

  // Insert market data
  createMarketTable() {
    this.storage.query(CREATE_STOCK_MARKET_TABLE).then(() => {
      // KLSE exchange
      this.insertMarket(new StockMarket(0, StockConfig.KLSE_EXCHANGE_SYMBOL, StockConfig.KLSE_EXCHANGE_NAME));
    }, (error) => {
      // No action
      // console.log('Error: ' + JSON.stringify(error.err));
    });
  }

  // Insert data for a particular market
  insertMarket(market: StockMarket) {
    // Check if the table is empty
    this.storage.query('SELECT COUNT(*) AS recordCount FROM market where symbol = ?', [market.symbol]).then((data) => {
      let count = data.res.rows.item(0).recordCount;
      if (count === 0) {
        this.storage.query('INSERT INTO market (symbol, name) VALUES (?, ?)', [market.symbol, market.name]).then((data) => {
          this.getMarket(market.symbol).then(data => {
            if (data.res.rows.length > 0) {
              let market = data.res.rows.item(0);
              // Insert stocks related to this exchange
              this.insertStocks(market.id, market.symbol);
            }
          });
        });
      }
    });
  }

  // Insert stocks for a particular market
  insertStocks(marketId: number, marketSymbol: string) {
    // Insert stocks for the particular market
    this.http.get('data/' + marketSymbol + '.json').subscribe(res => {
      let stocks = res.json();
      // Create the stock table
      this.storage.query(CREATE_STOCK_TABLE).then(() => {
        stocks.forEach(jsonObj => {
          let stock = new Stock(0, jsonObj.symbol, jsonObj.name, marketId);
          this.insertStock(marketId, stock);
        });
      });      
    });
  }

  // Insert a stock
  insertStock(marketId: number, stock: Stock){
    this.storage.query('INSERT INTO stock(symbol, name, market_id) VALUES(?, ?, ?)', [stock.symbol, stock.name, marketId]);
  }

  getStocks(marketId: number) {
    return this.storage.query('SELECT * FROM stock WHERE market_id = ?', [marketId]);
  }
}

