/**
 * Node.js script to insert stocks into Cassandra database.
 * 
 */

"use strict";

const CASSANDRA_HOST = 'localhost';
const CASSANDRA_KEYSPACE = 'myinvestor';

const cassandra = require('cassandra-driver');
const assert = require('assert');
const fs = require('fs');


if (process.argv.length !== 3) {
    console.error("Please pass in the stock exchange symbol file");
    process.exit(1);
}

const filePath = process.argv[2];
try {
    var stats = fs.statSync(filePath);
    if (!stats.isFile()) {
        console.log('file not exist');
        process.exit(1);
    }
} catch (e) {
    console.log('File not exists');
    process.exit(1);
}

/*
const client = new cassandra.Client({ contactPoints: [CASSANDRA_HOST], keyspace: CASSANDRA_KEYSPACE });

const query = 'SELECT exchange_id, exchange_name FROM exchange WHERE exchange_name =?';
client.execute(query, ['KLSE'], function (err, result) {
    if (err) {

    }
    console.log('got user profile with email ' + result.rows[0].exchange_id);
    client.shutdown();
});
*/

