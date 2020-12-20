insert into card (pan, security_code, expiration_date, available_funds, reserved_funds)
values ('1703456879237581', '345', '2025-10', 20, 0);
insert into card (pan, security_code, expiration_date, available_funds, reserved_funds)
values ('1703456879666668', '325', '2022-01', 0, 0);

insert into bank (code) values ('170');

insert into card_owner (type, merchant_id, merchant_email, password, success_url, failed_url, error_url, card_id)
values ('Merchant', '46QfIZh9KGe62AMDAStgnRbsK1fcX4', 'merchant@maildrop.cc', 'Merchant123!', 'http://localhost:3000/success', 'http://localhost:3000/failed', 'http://localhost:3000/error', 2);
insert into card_owner (type, name, last_name, email, card_id) values ('Client', 'Jovan', 'Jovanovic', 'jovanj@maildrop.cc', 1);
