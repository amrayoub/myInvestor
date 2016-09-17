
// Stock market 
export class StockMarket {
  id: number;
  symbol: string;
  name: string;

  constructor(id: number, symbol: string, name: string) {
    this.id = id;
    this.symbol = symbol;
    this.name = name;
  }
}

// Stock
export class Stock {
  id: number;
  symbol: string;
  name: string;
  marketId: number;

  constructor(id: number, symbol: string, name: string, marketId: number){
    this.id = id;
    this.symbol = symbol;
    this.name = name;
    this.marketId = marketId;
  }
}

// Dividend
export class Dividend {
    id: number;
    stockId: number;   
    dividendTypeId: number;
    dividend: string;
    period: string;
}

// Dividend type - percentage, cents, etc.
export class DividendType {
    id: number;
    name: string;
}

// Stock quote
export class Quote {
  id: number;
  stockId: number;
  price: number;
}