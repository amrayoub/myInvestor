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
    // Get the exchange id
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


