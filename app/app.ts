import { Component, ViewChild } from '@angular/core';
import { Events, ionicBootstrap, MenuController, Platform, Nav } from 'ionic-angular';
import { StatusBar } from 'ionic-native';

import { WatchlistPage } from './pages/watch-list/watch-list';
import { PortfolioPage } from './pages/portfolio/portfolio';
import { StockAnalysisPage } from './pages/stock-analysis/stock-analysis';

import { AboutPage } from './pages/about/about';
import { SettingsPage } from './pages/settings/settings';

import { StockService } from './providers/stock-service/stock-service';

interface PageObj {
  title: string;
  component: any;
  icon: string;
  index?: number;
}

@Component({
  templateUrl: 'build/app.html'
})
class MyInvestorApp {
  @ViewChild(Nav) nav: Nav;

  rootPage: any = StockAnalysisPage;

  appPages: PageObj[];
  otherPages: PageObj[];

  constructor(  
      public events: Events,
      public menu: MenuController, 
      public platform: Platform,
      public stockService: StockService
      ) {
    
    this.initializeApp();

    // App pages
    this.appPages = [
      { title: 'Watchlist', component: WatchlistPage, index: 1, icon: 'eye' },
      { title: 'Portfolio', component: PortfolioPage, index: 2, icon: 'briefcase' },
      { title: 'Stock Analysis', component: StockAnalysisPage, index: 3, icon: 'analytics' }    
    ];

    // Other pages
    this.otherPages = [
      { title: 'Settings', component: SettingsPage, index: 1, icon: 'settings' },
      { title: 'About', component: AboutPage, index: 2, icon: 'information-circle' }
    ];

    // Load any required data
    

  }

  initializeApp() {
    this.platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      StatusBar.styleDefault();
    });
  }

  openPage(page) {
    // Reset the content nav to have just this page
    // we wouldn't want the back button to show in this scenario
    if (page.index) {
      this.nav.setRoot(page.component, {tabIndex: page.index});
    } else {
      this.nav.setRoot(page.component);
    }
  }
}

ionicBootstrap(MyInvestorApp, [StockService], { });

