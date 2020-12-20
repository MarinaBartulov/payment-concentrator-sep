insert into card_owner (type, merchant_id, merchant_email, password, success_url, failed_url, error_url)
values ('Merchant', '46QfIZh9KGe62AMDAStgnRbsK1fcX4', 'merchant@maildrop.cc', 'Merchant123!', 'http://localhost:3000/success', 'http://localhost:3000/failed', 'http://localhost:3000/error');

insert into card_owner (type, name, last_name, email) values ('Client', 'Jovan', 'Jovanovic', 'jovanj@maildrop.cc');

insert into card (pan, security_code, expiration_date, available_funds, reserved_funds, card_holder_id)
values ('1703456879237581', '345', '2025-10', 30000, 0, 2);
insert into card (pan, security_code, expiration_date, available_funds, reserved_funds, card_holder_id)
values ('1703456879666668', '325', '2022-01', 0, 0, 1);

insert into bank (code) values ('170');
