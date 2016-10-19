import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

/*
  Generated class for the Watchlist page.

  See http://ionicframework.com/docs/v2/components/#navigation for more info on
  Ionic pages and navigation.
*/
@Component({
  selector: 'page-watchlist',
  templateUrl: 'watchlist.html'
})
export class Watchlist {

  constructor(public navCtrl: NavController) {}

  ionViewDidLoad() {
    console.log('Hello Watchlist Page');
  }

}
