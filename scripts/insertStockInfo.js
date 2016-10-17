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
                        getNumberValue(details.beta), getNumberValue(details.change)
                        ];
                        console.log(params);
                        /*
                        client.execute(insert, params, { prepare: true }, function (err, result) {
                            if (err) {
                                // Do nothing
                            }
                            if (++stockCount == stocks.length) {
                                next();
                            }
                        });
                        */
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

function zeroFill(number, width) {
    width -= number.toString().length;
    if (width > 0) {
        return new Array(width + (/\./.test(number) ? 2 : 1)).join('0') + number;
    }
    return number + ""; // always return a string
}

function parseDate(s) {
    var months = {
        jan: 1, feb: 2, mar: 3, apr: 4, may: 5, jun: 6,
        jul: 7, aug: 8, sep: 9, oct: 10, nov: 11, dec: 12
    };
    var p = s.split('-');
    if (p.length >= 3)
        return (parseInt(p[2]) + 2000) + "-" + zeroFill(months[p[1].toLowerCase()], 2) + "-" + zeroFill(p[0], 2);
    return '';
}