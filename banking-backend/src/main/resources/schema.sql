-- Many transactions point to one ACCOUNT, whcih many of can point to one USER


-- USERS TABLE
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    firstname TEXT NOT NULL,
    lastname TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE
);

-- ACCOUNTS TABLE
CREATE TABLE IF NOT EXISTS accounts (
    id BIGSERIAL PRIMARY KEY, -- big int with auto-increment
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE, -- On delete of users, delete this.
    balance BIGINT NOT NULL DEFAULT 0, -- stored in pennies
    account_type TEXT NOT NULL,
    creation_date TIMESTAMPTZ NOT NULL DEFAULT NOW() -- Times with time zone
);

-- TRANSACTIONS TABLE
CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    amount BIGINT NOT NULL,
    sender_id BIGINT NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    recipient_id BIGINT REFERENCES accounts(id) ON DELETE SET NULL,
    transaction_type TEXT NOT NULL CHECK (transaction_type IN ('transfer', 'deposit', 'withdraw')),
    description TEXT,
    timestamp TIMESTAMPTZ NOT NULL DEFAULT NOW()
);