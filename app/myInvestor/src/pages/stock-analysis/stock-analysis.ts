import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

/*
  Generated class for the StockAnalysis page.

  See http://ionicframework.com/docs/v2/components/#navigation for more info on
  Ionic pages and navigation.
*/
@Component({
  selector: 'page-stock-analysis',
  templateUrl: 'stock-analysis.html'
})
export class StockAnalysis {

  constructor(public navCtrl: NavController) {}

  ionViewDidLoad() {
    console.log('Hello StockAnalysis Page');
  }

}
