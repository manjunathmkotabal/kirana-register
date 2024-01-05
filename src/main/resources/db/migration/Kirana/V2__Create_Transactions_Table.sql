-- V1__Create_Transactions_Table.sql

CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    amountINR DECIMAL(10, 2),
    amountUSD DECIMAL(10, 2),
    currency VARCHAR(3),
    transactionDate TIMESTAMP
);
