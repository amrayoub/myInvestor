/**
 * Node.js script to insert stock history into Cassandra database.
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
        function insert(next) {
            if (exchangeId === '') next();
            const query = 'SELECT stock_symbol, stock_name FROM stock WHERE exchange_id = ?';
            client.eachRow(query, [exchangeId], { prepare: true },
                function (n, row) {
                    // Retrieve the stock history
                    var filePath = exchangeName + path.sep + row.stock_symbol + ".csv";
                    console.log('Inserting for ' + row.stock_symbol);
                    var stats = fs.statSync(filePath);
                    if (stats.isFile()) {
                        // Read the file
                        var content = fs.readFileSync(filePath, "utf-8");
                        var histories = csvToArray(content);
                        if (histories.length > 5) {
                            for (var i = 0; i < histories.length; i++) {
                                var insert = 'INSERT INTO stock_history (stock_symbol, history_date, history_open, history_high, history_low, history_close, history_volume) VALUES (?, ?, ?, ?, ?, ?, ?)';
                                // console.log(stock.symbol);
                                client.execute(insert, [row.stock_symbol, parseDate(histories[i].Date), 
                                            getNumberValue(histories[i].Open), getNumberValue(histories[i].High),
                                            getNumberValue(histories[i].Low), getNumberValue(histories[i].Close),
                                            getNumberValue(histories[i].Volume)], { prepare: true }, function (err, result) {
                                    if (err) {
                                        // Do nothing
                                    }
                                    if (++counter == stocks.length) {
                                        next();
                                    }
                                });
                            }
                        }
                    }
                },
                function (err) {
                    if (err) return next(err);
                    next();
                }
            );

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

function csvToArray(csvString) {
    var csvArray = [];
    var csvRows = csvString.split(/\n/);
    var csvHeaders = csvRows.shift().split(',');
    for (var rowIndex = 0; rowIndex < csvRows.length; ++rowIndex) {
        var rowArray = csvRows[rowIndex].split(',');
        var rowObject = csvArray[rowIndex] = {};
        for (var propIndex = 0; propIndex < rowArray.length; ++propIndex) {
            var propValue = rowArray[propIndex].replace(/^"|"$/g, '');
            var propLabel = csvHeaders[propIndex].replace(/^"|"$/g, '');
            rowObject[propLabel] = propValue;
        }
    }
    return csvArray;
}