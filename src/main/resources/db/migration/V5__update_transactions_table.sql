-- Ubah tipe data payment_date dari bigint ke timestamp
ALTER TABLE transactions
MODIFY COLUMN payment_date TIMESTAMP;