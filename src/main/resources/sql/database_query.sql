/* Plugin Name: EnhancedEconomy */

-- Create the table "constants", where are stored all the server constants
CREATE TABLE IF NOT EXISTS constants (
    constant_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name TEXT NOT NULL UNIQUE,
    value TEXT NOT NULL
);

-- Create the table "players", where are stored the player datas
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

-- Create the table "banks", where are stored the server's banks
CREATE TABLE IF NOT EXISTS banks (
    bank_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name TEXT NOT NULL,
    owner_id INTEGER NOT NULL REFERENCES users(user_id),
    balance DECIMAL(60,10) NOT NULL DEFAULT 0
);

-- Create the table "central_banks", where are stored the server's central banks
CREATE TABLE IF NOT EXISTS central_banks (
    central_bank_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    bank_id INTEGER NOT NULL UNIQUE REFERENCES banks(bank_id),
    name TEXT NOT NULL,
    owner_id INTEGER REFERENCES users(user_id),
    balance DECIMAL(60,10) NOT NULL DEFAULT 0,
    CHECK (balance >= 0)
);

-- Create the table "bank_accounts", where are stored the user's bank accounts
CREATE TABLE IF NOT EXISTS bank_accounts (
    account_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id INTEGER NOT NULL REFERENCES users(user_id),
    bank_id INTEGER NOT NULL REFERENCES banks(bank_id),
    displayed_name TEXT NOT NULL UNIQUE,
    sha512_pwd TEXT NOT NULL,
    registration_date DATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP),
    balance DECIMAL(60,10) NOT NULL DEFAULT 0
);

-- Create the table "bank_items", where are stored all the account's items
CREATE TABLE IF NOT EXISTS bank_items (
    item_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    account_id INTEGER NOT NULL REFERENCES bank_accounts(account_id),
    item TEXT NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    value DECIMAL(60,10) NOT NULL DEFAULT 0,
    CHECK(quantity > 0),
    CHECK(value >= 0)
);

-- Create the table "transactions", where are stored all the account's transactions (NOTE: the field "amount" is intended as the amount of money that were transferred)
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    source INTEGER NOT NULL REFERENCES bank_accounts(account_id),
    destination INTEGER NOT NULL REFERENCES bank_accounts(account_id),
    name TEXT NOT NULL,
    description TEXT,
    date DATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP),
    amount DECIMAL(60,10) NOT NULL,
    state TEXT NOT NULL,
    CHECK(amount > 0)
);

-- Create the table "offers", where are stored all the account's offers
CREATE TABLE IF NOT EXISTS offers (
    offer_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    seller INTEGER NOT NULL REFERENCES bank_accounts(account_id),
    item_id INTEGER NOT NULL REFERENCES bank_items(item_id),
    quantity INTEGER NOT NULL DEFAULT 1,
    price DECIMAL(60,10) NOT NULL,
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