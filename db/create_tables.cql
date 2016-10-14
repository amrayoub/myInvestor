
CREATE KEYSPACE myInvestor
WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 3};

CREATE TABLE exchange
(
exchange_id INT,
exchange_name TEXT,
PRIMARY KEY(exchange_id)
);

CREATE INDEX exchange_idx1 ON myinvestor.exchange(exchange_name);

INSERT INTO exchange(exchange_id, exchange_name) VALUES(1, 'KLSE');

CREATE TABLE stock
(
stock_symbol TEXT,
stock_name TEXT,
exchange_id INT,
PRIMARY KEY(stock_symbol)
);
CREATE INDEX stock_idx1 ON myinvestor.stock(exchange_id);


CREATE TABLE stock_history
(
stock_symbol TEXT,
history_date TIMESTAMP,
history_open DECIMAL,
history_high DECIMAL,
history_low DECIMAL,
history_close DECIMAL,
history_volume INT,
PRIMARY KEY(stock_symbol, history_date)
);



