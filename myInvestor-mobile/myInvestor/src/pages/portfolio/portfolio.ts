import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

/*
  Generated class for the Portfolio page.

  See http://ionicframework.com/docs/v2/components/#navigation for more info on
  Ionic pages and navigation.
*/
@Component({
  selector: 'page-portfolio',
  templateUrl: 'portfolio.html'
})
export class Portfolio {

  constructor(public navCtrl: NavController) {}

  ionViewDidLoad() {
    console.log('Hello Portfolio Page');
  }

}
