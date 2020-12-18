insert into merchant (merchant_id, merchant_email, password, error_url, failed_url, success_url)
values ('46QfIZh9KGe62AMDAStgnRbsK1fcX4', 'merchant@maildrop.cc', 'Merchant123!', 'http://localhost:3000/error', 'http://localhost:3000/failed', 'http://localhost:3000/success');

insert into card (pan, available_funds, card_holder_name, expiration_date, reserved_funds, security_code)
values ('1111111111111111', 10000, 'Bojana K', '2022-10', 300, '111');