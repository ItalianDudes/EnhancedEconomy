/* Plugin Name: EnhancedEconomy */

-- Create the table "constants", where are stored all the server constants
CREATE TABLE IF NOT EXISTS constants (
    constant_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name TEXT NOT NULL UNIQUE,
    value TEXT NOT NULL
);

-- Create the table "players", where are stored the player data
CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name TEXT NOT NULL UNIQUE,
    creation_date DATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP)
);

-- Define Constants
INSERT INTO constants (name, value) VALUES
("CUSTOM_WALLETS_STATE_SUCCESS", "SUCCESS"),
("CUSTOM_WALLETS_STATE_FAIL", "FAIL"),
("CUSTOM_WALLETS_STATE_AVAILABLE", "AVAILABLE"),
("CUSTOM_WALLETS_STATE_NOT_AVAILABLE", "NOT_AVAILABLE");

-- Create the table "currencies", where are stored the server currencies
CREATE TABLE IF NOT EXISTS currencies (
    currency_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name TEXT NOT NULL UNIQUE,
    symbol CHAR NOT NULL UNIQUE,
    iso TEXT NOT NULL UNIQUE,
    creation_date DATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP)
);

-- Create the table "countries", where are stored the server countries
CREATE TABLE IF NOT EXISTS countries (
    country_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name TEXT NOT NULL UNIQUE,
    creation_date DATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP)
);

-- Create the table "banks", where are stored the server banks
CREATE TABLE IF NOT EXISTS banks (
    bank_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name TEXT NOT NULL,
    country_id INTEGER NOT NULL REFERENCES countries(country_id),
    owner_id INTEGER REFERENCES users(user_id),
    creation_date DATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP)
);

-- Create the table "bank_currencies", where are stored the server banks
CREATE TABLE IF NOT EXISTS bank_currencies (
    bank_currency_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    bank_id INTEGER NOT NULL REFERENCES banks(bank_id),
    currency_id INTEGER NOT NULL REFERENCES currencies(currency_id),
    balance INTEGER NOT NULL DEFAULT 0,
    CHECK(balance >= 0),
    UNIQUE(bank_id, currency_id)
);

-- Create the table "exchange_rules", where are stored all currency exchange rules (NB: must exists 2 rules for entry, one for source to dest, the other for dest to source)
CREATE TABLE IF NOT EXISTS exchange_rules (
    exchange_rule_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    source_currency INTEGER REFERENCES currencies(currency_id),
    destination_currency INTEGER REFERENCES currencies(currency_id),
    price_ratio DECIMAL(60,10) NOT NULL DEFAULT 0,
    UNIQUE(source_currency, destination_currency)
);

-- Create the table "central_banks", where are stored the server's central banks
CREATE TABLE IF NOT EXISTS central_banks (
    central_bank_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name TEXT NOT NULL UNIQUE,
    owner_id INTEGER REFERENCES users(user_id),
    owned_currency INTEGER NOT NULL REFERENCES currencies(currency_id),
    balance DECIMAL(60,10) NOT NULL DEFAULT 0,
    creation_date DATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP)
    CHECK (balance >= 0)
);

-- Create the table "central_banks_countries", where are stored the server's central bank countries
CREATE TABLE IF NOT EXISTS central_banks_countries (
    central_bank_country_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    central_bank_id INTEGER NOT NULL REFERENCES central_banks(central_bank_id),
    country_id INTEGER NOT NULL REFERENCES countries(country_id),
    UNIQUE(central_bank_id, country_id)
);

-- Create the table "bank_accounts", where are stored the user's bank accounts
CREATE TABLE IF NOT EXISTS bank_accounts (
    account_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id INTEGER NOT NULL REFERENCES users(user_id),
    bank_id INTEGER NOT NULL REFERENCES banks(bank_id),
    displayed_name TEXT NOT NULL UNIQUE,
    sha512_pwd TEXT NOT NULL,
    creation_date DATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP)
);

CREATE TABLE IF NOT EXISTS accounts_currencies (
    account_currency_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    account_id INTEGER NOT NULL REFERENCES bank_accounts(account_id),
    currency_id INTEGER NOT NULL REFERENCES currencies(currency_id),
    balance INTEGER NOT NULL DEFAULT 0,
    CHECK(balance >= 0),
    UNIQUE(account_id, currency_id)
);


-- Create the table "bank_items", where are stored all the account's items
CREATE TABLE IF NOT EXISTS bank_items (
    item_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    account_id INTEGER NOT NULL REFERENCES bank_accounts(account_id),
    item TEXT NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    CHECK(quantity > 0),
    UNIQUE(account_id, item)
);

-- Create the table "transactions", where are stored all the account's transactions (NOTE: the field "amount" is intended as the amount of money that were transferred)
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    source INTEGER NOT NULL REFERENCES bank_accounts(account_id),
    destination INTEGER NOT NULL REFERENCES bank_accounts(account_id),
    name TEXT NOT NULL,
    description TEXT,
    date DATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP),
    source_amount DECIMAL(60,10) NOT NULL,
    source_currency INTEGER NOT NULL REFERENCES currencies(currency_id),
    destination_amount DECIMAL(60,10) NOT NULL,
    destination_currency INTEGER NOT NULL REFERENCES currencies(currency_id),
    state TEXT NOT NULL,
    CHECK(source_amount > 0),
    CHECK(destination_amount > 0)
);

-- Create the table "offers", where are stored all the account's offers
CREATE TABLE IF NOT EXISTS offers (
    offer_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    seller INTEGER NOT NULL REFERENCES bank_accounts(account_id),
    item_id INTEGER NOT NULL REFERENCES bank_items(item_id),
    quantity INTEGER NOT NULL DEFAULT 1,
    price DECIMAL(60,10) NOT NULL,
    currency INTEGER NOT NULL REFERENCES currencies(currency_id),
    date DATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP),
    state TEXT NOT NULL,
    CHECK(quantity >= 0),
    CHECK(price >= 0)
);

-- Create the table "trades", where are stored all the trades
CREATE TABLE IF NOT EXISTS trades (
    trade_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    offer INTEGER NOT NULL REFERENCES offers(offer_id),
    buyer INTEGER NOT NULL REFERENCES bank_accounts(account_id),
    quantity INTEGER NOT NULL DEFAULT 1,
    date DATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP),
    state TEXT NOT NULL,
    CHECK(quantity > 0)
);