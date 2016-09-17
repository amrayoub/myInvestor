import { Component } from '@angular/core';
import { NavController, PopoverController, ViewController  } from 'ionic-angular';
import { InAppBrowser } from 'ionic-native';

@Component({
  template: `
    <ion-list>
      <button ion-item (click)="openSite($event)">myInvestor</button>
    </ion-list>
  `
})
class PopoverPage {
  constructor(public viewCtrl: ViewController) { }

  close() {
    this.viewCtrl.dismiss();
  }

  openSite(event){
    InAppBrowser.open(`http://www.mymobkit.com`);
  }
}



/*
 About page.
*/
@Component({
  templateUrl: 'build/pages/about/about.html',
})
export class AboutPage {

  constructor(public popoverCtrl: PopoverController) { }

  presentPopover(event) {
    let popover = this.popoverCtrl.create(PopoverPage);
    popover.present({ ev: event });
  }
}
