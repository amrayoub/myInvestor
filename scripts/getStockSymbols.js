"use strict";

String.prototype.format = String.prototype.f = function () {
    var s = this,
        i = arguments.length;

    while (i--) {
        s = s.replace(new RegExp('\\{' + i + '\\}', 'gm'), arguments[i]);
    }
    return s;
};

var system = require('system'),
    page = require('webpage').create(),
    timestamp,
    targetUrl;

var GOOGLE_FINANCE_URL_GET_EXCHANGE_SYMBOLS = 'https://www.google.com/finance?q=%5B%28exchange+%3D%3D+%22{0}%22%29%5D&restype=company&noIL=1&num=2000&ei=5YbOV4ieA9exugTRyZOoCw';

if (system.args.length === 1) {
    console.log('Pass in the exchange name');
    phantom.exit(1);
} else {
    timestamp = Date.now();
    console.log('Getting stock symbols for [' + system.args[1] + ']');

    targetUrl = GOOGLE_FINANCE_URL_GET_EXCHANGE_SYMBOLS.format(system.args[1]);
    console.log('target url ' + targetUrl);
    page.open(targetUrl, function (status) {
        if (status !== 'success') {
            console.log('Failed to load the address');
        } else {
            console.log('Page title is ' + page.evaluate(function () {
                return document.title;
            }));
        }
        timestamp = Date.now() - timestamp;
        console.log('Total time: ' + (timestamp / 1000) + ' seconds');
        phantom.exit();
    });
}


