insert into bank (name, code) values ('Banca Intesa', '170'); -- 1
insert into bank (name, code) values ('UniCredit', '171'); -- 2

insert into card (pan, security_code, expiration_date, available_funds, reserved_funds)
values ('1703456879237581', '345', '2025-10', 20, 0); -- 1
insert into card (pan, security_code, expiration_date, available_funds, reserved_funds)
values ('1703456879666668', '325', '2022-01', 0, 0); -- 2
insert into card (pan, security_code, expiration_date, available_funds, reserved_funds)
values ('1713456879231111', '333', '2023-04', 5, 0); -- 3

insert into card_owner (type, name, merchant_id, merchant_email, password, success_url, failed_url, error_url, bank_id, card_id)
values ('Merchant', 'Laguna', 'jlSuwpyB4v62M7KBc4dXiXuwSg53AsnKtWV/zP9E0cA=|zj3rWlhdDkkqWcfAf9hMfw==', 'sb-nsr1z4072854@business.example.com', '4bgphCgirDKF3SYl4LvFRQ==|YA7u//HlmBJoA2EevRphGw==', 'https://localhost:3000/success', 'https://localhost:3000/failed', 'https://localhost:3000/error', 1, 2);

insert into card_owner (type, first_name, last_name, email, nin, bank_id, card_id) values ('Client', 'Jovan', 'Jovanovic', 'jovanj@maildrop.cc', '1245886359863', 1, 1);
insert into card_owner (type, first_name, last_name, email, nin, bank_id, card_id) values ('Client', 'Milan', 'Milanovic', 'milanm@maildrop.cc', '1245887759863', 2, 3);
