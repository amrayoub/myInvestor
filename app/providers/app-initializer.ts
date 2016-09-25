import { Injectable } from '@angular/core';

import { StockService } from './stock-service/stock-service';

@Injectable()
export class AppInitializer {

    constructor(public stockService: StockService) {

    }

    // Perform initial data loading
    load() {
        // For 1st time setup, create the necessary tables and populate the data
        this.stockService.createMarketTable();
    }
}