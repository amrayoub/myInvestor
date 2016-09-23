import { Injectable } from '@angular/core';
import { AlertController } from 'ionic-angular';

@Injectable()
export class AppConfig {

    appName: string;

    constructor(public alertCtrl: AlertController) {
        this.appName = 'myInvestor';
    }

    getAppName() {
        return this.appName;
    }
}