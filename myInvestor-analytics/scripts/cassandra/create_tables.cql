
CREATE KEYSPACE myInvestor
WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};

USE myInvestor;

CREATE TABLE request
(
    request_id UUID,
    success BOOLEAN,
    error_msg TEXT,
    PRIMARY KEY(request_id)
);

CREATE TABLE exchange
(
    exchange_name TEXT,
    PRIMARY KEY(exchange_name)
);

INSERT INTO exchange(exchange_name) VALUES('KLSE');
INSERT INTO exchange(exchange_name) VALUES('NYSE');

CREATE TABLE stock
(
    stock_symbol TEXT,
    exchange_name TEXT,
    stock_name TEXT,
    PRIMARY KEY(exchange_name, stock_symbol)
);

CREATE TABLE stock_history
(
    stock_symbol TEXT,
    exchange_name TEXT,
    history_date TIMESTAMP,
    history_open DECIMAL,
    history_high DECIMAL,
    history_low DECIMAL,
    history_close DECIMAL,
    history_volume INT,
    PRIMARY KEY(exchange_name, stock_symbol, history_date)
);

CREATE TABLE stock_details
(
    stock_symbol TEXT,
    exchange_name TEXT,
    details_52weeks_from DECIMAL,
    details_52weeks_to DECIMAL,
    details_beta DECIMAL,
    details_change DECIMAL,
    details_change_percentage TEXT,
    details_current_price DECIMAL,
    details_dividend_yield TEXT,
    details_eps DECIMAL,
    details_inst_own TEXT,
    details_market_capital TEXT,
    details_open DECIMAL,
    details_pe DECIMAl,
    details_range_from DECIMAL,
    details_range_to DECIMAL,
    details_shares TEXT,
    details_time TEXT,
    details_volume TEXT,
    details_extracted_timestamp TIMESTAMP,  
    PRIMARY KEY(exchange_name, stock_symbol, details_extracted_timestamp)
);



