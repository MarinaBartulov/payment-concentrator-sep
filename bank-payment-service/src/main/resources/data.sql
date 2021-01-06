insert into card (pan, security_code, expiration_date, available_funds, reserved_funds)
values ('1703456879237581', '345', '2025-10', 20, 0);
insert into card (pan, security_code, expiration_date, available_funds, reserved_funds)
values ('1703456879666668', '325', '2022-01', 0, 0);
insert into card (pan, security_code, expiration_date, available_funds, reserved_funds)
values ('1703456879231111', '333', '2023-04', 5, 0);

insert into bank (code) values ('170');

insert into card_owner (type, merchant_id, merchant_email, password, success_url, failed_url, error_url, card_id)
values ('Merchant', 'uK7Xfa1Aki10Tvu8gOMGN+iknFdURannsE5UUiuBCBc=|2cS8Md1TdbT41N+OGYslpA==', 'sb-nsr1z4072854@business.example.com', 'cesyX5oUERv5y7JJzapRFg==|cMV/dnhfgAN35z3d7W4Pug==', 'https://localhost:3000/success', 'https://localhost:3000/failed', 'https://localhost:3000/error', 2);
insert into card_owner (type, name, last_name, email, card_id) values ('Client', 'Jovan', 'Jovanovic', 'jovanj@maildrop.cc', 1);
insert into card_owner (type, name, last_name, email, card_id) values ('Client', 'Milan', 'Milanovic', 'milanm@maildrop.cc', 3);
