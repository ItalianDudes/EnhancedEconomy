/* Plugin Name: EnhancedEconomy */

-- Tables Declaration
-- Create the table "players", where are stored the player data
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    creation_date TIMESTAMP NOT NULL DEFAULT(CURRENT_TIMESTAMP)
);

-- Create the table "currencies", where are stored the server currencies
CREATE TABLE IF NOT EXISTS currencies (
    currency_id SERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    symbol TEXT NOT NULL,
    iso TEXT NOT NULL UNIQUE,
    creation_date TIMESTAMP NOT NULL DEFAULT(CURRENT_TIMESTAMP)
);

-- Create the table "countries", where are stored the server countries
CREATE TABLE IF NOT EXISTS countries (
    country_id SERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    owner_id INTEGER NOT NULL REFERENCES users(user_id),
    creation_date TIMESTAMP NOT NULL DEFAULT(CURRENT_TIMESTAMP)
);

-- Create the table "banks", where are stored the server banks
CREATE TABLE IF NOT EXISTS banks (
    bank_id SERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    headquarter_country INTEGER NOT NULL REFERENCES countries(country_id),
    is_private INTEGER NOT NULL DEFAULT 1,
    owner_id INTEGER NOT NULL REFERENCES users(user_id),
    creation_date TIMESTAMP NOT NULL DEFAULT(CURRENT_TIMESTAMP)
);

-- Create the table "bank_currencies", where are stored the server banks
CREATE TABLE IF NOT EXISTS bank_currencies (
    bank_currency_id SERIAL PRIMARY KEY,
    bank_id INTEGER NOT NULL REFERENCES banks(bank_id),
    currency_id INTEGER NOT NULL REFERENCES currencies(currency_id),
    balance DECIMAL(60,10) NOT NULL DEFAULT 0,
    CHECK(balance >= 0),
    UNIQUE(bank_id, currency_id)
);

-- Create the table "exchange_rules", where are stored all currency exchange rules (NB: must exists 2 rules for entry, one for source to dest, the other for dest to source)
CREATE TABLE IF NOT EXISTS exchange_rules (
    exchange_rule_id SERIAL PRIMARY KEY,
    source_currency INTEGER REFERENCES currencies(currency_id),
    destination_currency INTEGER REFERENCES currencies(currency_id),
    price_ratio DECIMAL(60,10) NOT NULL DEFAULT 1,
    UNIQUE(source_currency, destination_currency)
);

-- Create the table "central_banks", where are stored the server's central banks
CREATE TABLE IF NOT EXISTS central_banks (
    central_bank_id SERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    owner_id INTEGER REFERENCES users(user_id),
    owned_currency INTEGER NOT NULL REFERENCES currencies(currency_id),
    balance DECIMAL(60,10) NOT NULL DEFAULT 0,
    creation_date TIMESTAMP NOT NULL DEFAULT(CURRENT_TIMESTAMP)
    CHECK (balance >= 0)
);

-- Create the table "central_banks_countries", where are stored the server's central bank countries
CREATE TABLE IF NOT EXISTS central_banks_countries (
    central_bank_country_id SERIAL PRIMARY KEY,
    central_bank_id INTEGER NOT NULL REFERENCES central_banks(central_bank_id),
    country_id INTEGER NOT NULL REFERENCES countries(country_id),
    UNIQUE(central_bank_id, country_id)
);

-- Create the table "bank_accounts", where are stored the user's bank accounts
CREATE TABLE IF NOT EXISTS bank_accounts (
    account_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id),
    bank_id INTEGER NOT NULL REFERENCES banks(bank_id),
    displayed_name TEXT NOT NULL UNIQUE,
    sha512_pwd TEXT NOT NULL,
    creation_date TIMESTAMP NOT NULL DEFAULT(CURRENT_TIMESTAMP)
);

-- Create the table "accounts_currencies", where are stored the account's currencies
CREATE TABLE IF NOT EXISTS accounts_currencies (
    account_currency_id SERIAL PRIMARY KEY,
    account_id INTEGER NOT NULL REFERENCES bank_accounts(account_id),
    currency_id INTEGER NOT NULL REFERENCES currencies(currency_id),
    balance DECIMAL(60,10) NOT NULL DEFAULT 0,
    CHECK(balance >= 0),
    UNIQUE(account_id, currency_id)
);

-- Create the table "bank_items", where are stored all the account's items
CREATE TABLE IF NOT EXISTS bank_items (
    item_id SERIAL PRIMARY KEY,
    account_id INTEGER NOT NULL,
    item TEXT NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    CHECK(quantity > 0),
    UNIQUE(item_id, account_id),
    FOREIGN KEY(account_id) REFERENCES bank_accounts(account_id)
);

-- Create the table "transactions", where are stored all the account's transactions (NOTE: the field "amount" is intended as the amount of money that were transferred)
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id SERIAL PRIMARY KEY,
    source INTEGER NOT NULL REFERENCES bank_accounts(account_id),
    destination INTEGER NOT NULL REFERENCES bank_accounts(account_id),
    name TEXT NOT NULL,
    description TEXT,
    date TIMESTAMP NOT NULL DEFAULT(CURRENT_TIMESTAMP),
    source_amount DECIMAL(60,10) NOT NULL,
    source_currency INTEGER NOT NULL REFERENCES currencies(currency_id),
    destination_amount DECIMAL(60,10) NOT NULL,
    destination_currency INTEGER NOT NULL REFERENCES currencies(currency_id),
    state TEXT NOT NULL,
    CHECK(source_amount >= 0),
    CHECK(destination_amount >= 0)
);

-- Create the table "offers", where are stored all the account's offers
CREATE TABLE IF NOT EXISTS offers (
    offer_id SERIAL PRIMARY KEY,
    seller INTEGER NOT NULL REFERENCES bank_accounts(account_id),
    item_id INTEGER NOT NULL REFERENCES bank_items(item_id),
    quantity INTEGER NOT NULL DEFAULT 1,
    price DECIMAL(60,10) NOT NULL,
    currency INTEGER NOT NULL REFERENCES currencies(currency_id),
    date TIMESTAMP NOT NULL DEFAULT(CURRENT_TIMESTAMP),
    state TEXT NOT NULL,
    CHECK(quantity >= 0),
    CHECK(price >= 0)
);

-- Create the table "trades", where are stored all the trades
CREATE TABLE IF NOT EXISTS trades (
    trade_id SERIAL PRIMARY KEY,
    offer INTEGER NOT NULL REFERENCES offers(offer_id),
    buyer INTEGER NOT NULL REFERENCES bank_accounts(account_id),
    quantity INTEGER NOT NULL DEFAULT 1,
    date TIMESTAMP NOT NULL DEFAULT(CURRENT_TIMESTAMP),
    state TEXT NOT NULL,
    CHECK(quantity > 0)
);

-- Views Declaration
-- Create view "users_accounts_banks", where are filtered the users, their account's name, and the bank name
CREATE OR REPLACE VIEW v_users_accounts_banks AS (
    SELECT
        j.name AS PlayerName, j.displayed_name AS AccountName,
        b.name AS Bank, j.user_id AS PlayerID,
        j.account_id AS AccountID, b.bank_id AS BankID
    FROM
    (
        SELECT u.user_id, name, displayed_name, bank_id, account_id
        FROM users AS u
        JOIN bank_accounts AS ba ON u.user_id = ba.user_id
    ) AS j
    JOIN banks AS b ON j.bank_id = b.bank_id
);

-- Create view "account_currencies", where are filtered the the account's name, the balance and the value iso
CREATE OR REPLACE VIEW v_accounts_currencies AS
(
    SELECT
        j.displayed_name AS AccountName, j.balance AS Balance,
        c.iso AS ISO, j.account_id AS AccountID,
        j.account_currency_id AS BalanceID, j.currency_id AS CurrencyID
FROM (
        SELECT displayed_name, balance, currency_id, ac.account_id, account_currency_id
        FROM bank_accounts AS ba
        JOIN accounts_currencies ac ON ba.account_id = ac.account_id
    ) AS j
    JOIN currencies AS c ON j.currency_id = c.currency_id
);

-- Create view "account_balances", where are filtered the account's name, the player name, the balance and the iso, all with the ids
CREATE OR REPLACE VIEW v_accounts_balances AS (
    SELECT
        ub.PlayerName AS PlayerName, ub.AccountName AS AccountName,
        ub.Bank As Bank, acv.Balance AS Balance,
        acv.ISO AS ISO, ub.PlayerID AS PlayerID,
        ub.AccountID AS AccountID, ub.BankID AS BankID,
        acv.BalanceID AS BalanceID, acv.CurrencyID AS CurrencyID
    FROM v_users_accounts_banks AS ub
    JOIN v_accounts_currencies AS acv ON ub.AccountName = acv.AccountName
);

-- Create view "bank_currencies", where are filtered the bank's name, the balance and the iso, all with the ids
CREATE OR  REPLACE VIEW v_bank_currencies AS (
    SELECT
        j.BankName AS BankName, j.balance AS Balance,
        c.iso AS ISO, j.bank_id AS BankID,
        j.BalanceID AS BalanceID, c.currency_id AS CurrencyID
    FROM (
        SELECT b.name AS BankName, bc.balance, bc.bank_id, bc.currency_id, bc.bank_currency_id AS BalanceID
        FROM bank_currencies AS bc
        JOIN banks AS b ON b.bank_id = bc.bank_id
    ) AS j
    JOIN currencies AS c ON c.currency_id = j.currency_id
);

-- Procedures Declaration
-- Create procedure "accounts_currencies_checker_procedure()", used in the trigger "accounts_currencies_checker"
CREATE OR REPLACE FUNCTION accounts_currencies_checker_procedure()
    RETURNS TRIGGER
    LANGUAGE PLPGSQL
    AS
$$
BEGIN
    IF NEW.currency_id NOT IN (
        SELECT vbc1.CurrencyID
        FROM v_bank_currencies AS vbc1
        WHERE vbc1.BankID = (
            SELECT ba.bank_id
            FROM bank_accounts AS ba
            WHERE ba.account_id = NEW.account_id
        )
    ) THEN
        RAISE EXCEPTION 'Cannot insert this account currency: the bank does not support the currency';
    END IF;
END
$$;

-- Create procedure "bank_balance_updater_procedure()", used in the trigger "bank_balance_updater"
CREATE OR REPLACE FUNCTION bank_balance_updater_procedure()
    RETURNS TRIGGER
    LANGUAGE PLPGSQL
    AS
$$
BEGIN
    UPDATE bank_currencies
    SET balance = (
        SELECT SUM(vab1.balance)
        FROM v_accounts_balances AS vab1
        WHERE
            vab1.CurrencyID = NEW.currency_id
            AND
            vab1.BankID = (
                SELECT ba.bank_id
                FROM bank_accounts AS ba
                WHERE ba.account_id = NEW.account_id
            )
    )
    WHERE
        bank_currencies.currency_id = NEW.currency_id
        AND
        bank_currencies.bank_id = (
            SELECT ba.bank_id
            FROM bank_accounts AS ba
            WHERE ba.account_id = NEW.account_id
        );
END;
$$;

-- Triggers Declaration
-- Create trigger "accounts_currencies_checker", that prevents the insert of account currencies if the bank doesn't support the currency
DROP TRIGGER IF EXISTS accounts_currencies_checker ON accounts_currencies;
CREATE TRIGGER accounts_currencies_checker
    BEFORE INSERT
    ON accounts_currencies FOR EACH ROW
    EXECUTE PROCEDURE accounts_currencies_checker_procedure();

-- Create trigger "bank_balance_updater", that updates tha bank's balances on user's accounts balance update
DROP TRIGGER IF EXISTS bank_balance_updater ON accounts_currencies;
CREATE TRIGGER bank_balance_updater
    AFTER UPDATE
    ON accounts_currencies FOR EACH ROW
    EXECUTE PROCEDURE bank_balance_updater_procedure();