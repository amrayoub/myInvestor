import { Component } from '@angular/core';
import { NavController, NavParams, ViewController, ToastController } from 'ionic-angular';

import { StockMarket, Stock } from '../../providers/stock-service/stock-model';
import { StockService, StockConfig } from '../../providers/stock-service/stock-service';


export class StockAnalysis {
  currentStockIndex: number = 10;  // Current stock being analyzed
  currentStockSymbol: string;
  currentStockName: string;
  marketName: string;
  totalStocks: number = 100;     //  Total stocks to be analyzed

  constructor() {

  }
}

/*
  Stock analysis page.
*/
@Component({
  templateUrl: 'build/pages/stock-analysis/stock-analysis.html',
})
export class StockAnalysisPage {

  markets: StockMarket[]; // List of support markets
  targetMarket: string;   // Market to be analyzed
  stockAnalysis: StockAnalysis; // Stock analysis
  hideProcessingPanel: boolean = true;

  constructor(
    public navCtrl: NavController,
    public navParams: NavParams,
    public viewCtrl: ViewController,
    public toastCtrl: ToastController,
    public stockService: StockService
  ) {
    this.targetMarket = StockConfig.KLSE_EXCHANGE_SYMBOL; // Default to KLSE  
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


  performAnalysis(event) {
    // console.log(this.targetMarket);
    
    // Show the processing panel
    this.hideProcessingPanel = false;

    //this.stockService.getStocks().then(data => {

    //});

  }

}
