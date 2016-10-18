/**
 * Insert stock info into Cassandra.
 * 
 * 
 */
"use strict";

const CASSANDRA_HOST = 'localhost';
const CASSANDRA_KEYSPACE = 'myinvestor';

const cassandra = require('cassandra-driver');
const assert = require('assert');
const fs = require('fs');
const path = require('path');
const async = require('async');

if (process.argv.length !== 3) {
    console.error("Please pass in the exchange name");
    process.exit(1);
}
const exchangeName = process.argv[2];
try {
    const client = new cassandra.Client({ contactPoints: [CASSANDRA_HOST], keyspace: CASSANDRA_KEYSPACE });
    var exchangeId = '';
    var stocks = [];
    async.series([
        function connect(next) {
            console.log('Connecting to Cassandra');
            client.connect(next);
        },
        function getExchangeId(next) {
            const query = 'SELECT exchange_id, exchange_name FROM exchange WHERE exchange_name = ?';
            client.execute(query, [exchangeName], { prepare: true }, function (err, result) {
                if (err) return next(err);
                var row = result.first();
                if (row !== null)
                    exchangeId = row.exchange_id;
                next();
            });
        },
        function getStocks(next) {
            if (exchangeId === '') next();
            const query = 'SELECT stock_symbol, stock_name FROM stock WHERE exchange_id = ?';
            var counter = 0;
            client.eachRow(query, [exchangeId], { prepare: true },
                function (n, row) {
                    stocks.push(row);
                },
                function (err) {
                    if (err) return next(err);
                    next();
                }
            );
        },
        function insert(next) {
            var stockCount = 0;
            for (var counter = 0; counter < stocks.length; counter++) {
                var stock = stocks[counter];
                console.log('Updating stock info for ' + stock.stock_symbol);
                var filePath = exchangeName + path.sep + stock.stock_symbol + ".json";
                try {
                    var stats = fs.statSync(filePath);
                    if (stats.isFile()) {
                        var details = JSON.parse(fs.readFileSync(filePath, "utf-8"));
                        var insert = 'INSERT INTO stock_details (stock_symbol, details_52weeks_from, details_52weeks_to,' +
                            'details_beta,' +
                            'details_change,' +
                            'details_change_percentage,' +
                            'details_current_price,' +
                            'details_dividend_yield,' +
                            'details_eps,' +
                            'details_inst_own,' +
                            'details_market_capital,' +
                            'details_open,' +
                            'details_pe,' +
                            'details_range_from,' +
                            'details_range_to,' +
                            'details_shares,' +
                            'details_time,' +
                            'details_volume,' +
                            'details_extracted_timestamp' +
                            ') VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)';


                        var params = [stock.stock_symbol, getNumberFromArr(details._52Weeks, 0), getNumberFromArr(details._52Weeks, 1),
                        getNumberValue(details.beta), getNumberValue(details.change), getPercentageValue(details.changePercentage),
                        getNumberValue(details.current), getStringValue(details.dividendYield), getNumberValue(details.eps),
                        getPercentageValue(details.instOwn), getStringValue(details.marketCapital), getNumberValue(details.open),
                        getNumberValue(details.pe), getNumberFromArr(details.range, 0), getNumberFromArr(details.range, 1),
                        getStringValue(details.shares), getStringValue(details.time), getStringValue(details.volume), (new Date()).yyyymmdd()
                        ];
                        //console.log(params);
                        
                        client.execute(insert, params, { prepare: true }, function (err, result) {
                            if (err) {
                                // Do nothing
                                console.log(err);
                            } 
                            if (++stockCount == stocks.length) {
                                next();
                            }
                        });                        
                    } else {
                        if (++stockCount == stocks.length) {
                            next();
                        }
                    }
                } catch (e) {
                    console.log('Unable to process ' + filePath, e.message);
                }
            }
        }
    ], function (err) {
        if (err) {
            console.error('There was an error', err.message, err.stack);
        }
        console.log('Shutting down');
        client.shutdown();
    });
} catch (e) {
    console.error(e.message);
    process.exit(1);
}

function getPercentageValue(value) {
    return getNumberValue(value).toString().replace('%', '');
}

function getNumberFromArr(arr, index) {
    try {
        var values = arr.split('-');
        if (values.length > 0) {
            return parseFloat(values[index]);
        }
    } catch (e) {
        return 0;
    }
}

function getNumberValue(val) {
    if (val === '-') return 0;
    return val;
}

function getStringValue(val) {
    if (val === '-') return '';
    return val;
}

Date.prototype.yyyymmdd = function() {
  var mm = (this.getMonth() + 1).toString(); // getMonth() is zero-based
  var dd = this.getDate().toString();

  return (this.getFullYear()) + '-' +  (mm.length===2 ? '' : '0', mm)  + '-' + (dd.length===2 ? '' : '0', dd); // padding
};
