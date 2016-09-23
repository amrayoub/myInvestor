import { Component } from '@angular/core';
import { NavController, NavParams, ViewController } from 'ionic-angular';

import { StockMarket, Stock } from '../../providers/stock-service/stock-model';
import { StockService } from '../../providers/stock-service/stock-service';
import { AlertService } from '../../providers/helper-service/alert-service';

// Stock analysis
export class StockAnalysis {
  totalStocks: number = 0;        //  Total stocks to be analyzed
  currentStockIndex: number = 0;  // Current stock being analyzed
  currentMarket: StockMarket;
  currentStock: Stock;
  stocks: Stock[];

  constructor() {
    this.stocks = [];
    this.currentMarket = new StockMarket(0, '', '');
    this.currentStock = new Stock(0, '', '', 0);
  }
}

// Stock analysis page
@Component({
  templateUrl: 'build/pages/stock-analysis/stock-analysis.html',
})
export class StockAnalysisPage {

  markets: StockMarket[]; // List of support markets
  marketId: number;       // Market to be analyzed

  stockAnalysis: StockAnalysis; // Stock analysis
  hideProcessingPanel: boolean = true;

  constructor(
    public navCtrl: NavController,
    public navParams: NavParams,
    public viewCtrl: ViewController,
    public stockService: StockService,
    public alertService: AlertService
  ) {
    this.marketId = 1;    // Default market id
    this.stockAnalysis = new StockAnalysis();
    this.loadMarkets();   // Load market information
  }

  private loadMarkets() {
    this.stockService.getMarkets().then(data => {
      this.markets = [];
      if (data.res.rows.length > 0) {
        for (var i = 0; i < data.res.rows.length; i++) {
          let market = data.res.rows.item(i);
          this.markets.push(new StockMarket(market.id, market.symbol, market.name));
        }
      }
    });
  }


  // Perform stock analysis
  performAnalysis(event) {    
    // Retrieve all stocks related to this exchange
    this.stockService.getStocks(this.marketId).then(response => {
      if (response.res.rows.length > 0) {
        this.hideProcessingPanel = false;    // Show the processing panel
        this.stockAnalysis.currentMarket = this.markets.find(market => market.id === this.marketId);
        this.stockAnalysis.totalStocks = response.res.rows.length;
        for (var i = 0; i < response.res.rows.length; i++) {
          let stock = response.res.rows.item(i);
          this.stockAnalysis.currentStockIndex = i + 1;
          this.stockAnalysis.currentStock = new Stock(stock.id, stock.symbol, stock.name, stock.market_id);
          this.stockAnalysis.stocks.push(this.stockAnalysis.currentStock);

          // Process each stock
        }
      } else {
        // No stocks found, display an error message
        this.alertService.showAlert('No stocks found');
      }
    });
  }

}
