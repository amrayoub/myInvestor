import { Injectable } from '@angular/core';
import { AlertController } from 'ionic-angular';
import { AppConfig } from '../app-config';

@Injectable()
export class AlertService {    
    
    constructor(
        public alertCtrl: AlertController,
        public appConfig: AppConfig
        ) {
       
    }

    showAlert(msg) {
        let alert = this.alertCtrl.create({
            title: '',
            subTitle: msg,
            buttons: ['OK']
        });
        alert.present();
    }
}