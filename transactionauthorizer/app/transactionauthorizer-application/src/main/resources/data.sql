-- init.sql

  INSERT INTO account (id, created_at, email, name) VALUES ('1',  '2024-12-15 12:00:00', 'meu@email.com', 'Meu nome');
  INSERT INTO wallet (id, type, amount, account_id) VALUES ('1',  'CASH', 100.00, '1');
  INSERT INTO wallet (id, type, amount, account_id) VALUES ('2',  'MEAL', 100.00, '1');
  INSERT INTO wallet (id, type, amount, account_id) VALUES ('3',  'FOOD', 100.00, '1');
