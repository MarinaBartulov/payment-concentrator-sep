insert into bank (name, code) values ('Banca Intesa', '170'); -- 1
insert into bank (name, code) values ('UniCredit', '171'); -- 2

-- svima je security_code 345
-- pan: 1703456879237581
insert into card (pan, security_code, expiration_date, available_funds)
values ('kUBnwJSg6d2isRCRvYzYE61vKUe2z+4O22FBixsF7cg=|/UlpTO7y7cxpB7NXKGhW+A==', '3jiq7E30G9ouTkPdt5Zcww==|D5B9oxxyyktAJAQRB0aqew==', '2025-10', 20); -- 1 zauzeta
-- pan: 1703456879666668
insert into card (pan, security_code, expiration_date, available_funds)
values ('vddiiFL8b1YJmmbcQC9FaRTSnvmLmVuiqkqIYhNjl68=|BJdJtARL7K/W4sOW/kr+iA==', '3jiq7E30G9ouTkPdt5Zcww==|D5B9oxxyyktAJAQRB0aqew==', '2022-01', 0); -- 2 zauzeta
-- pan: 1713456879231111
insert into card (pan, security_code, expiration_date, available_funds)
values ('k41fHKkINTIZ5VJ03lBFH7Dk6UJ1E5b8i1uIOm09kHc=|inEmHty9x7jocsNk5xFPgQ==', '3jiq7E30G9ouTkPdt5Zcww==|D5B9oxxyyktAJAQRB0aqew==', '2023-04', 50); -- 3 zauzeta
-- pan: 1701111879231111
insert into card (pan, security_code, expiration_date, available_funds)
values ('Jf+4YGPjQFgxzpK88/nH+cNXDeK7IZHBJaY0hBUMGB4=|CEJEVgd89QJe+1mhZeM6oQ==', '3jiq7E30G9ouTkPdt5Zcww==|D5B9oxxyyktAJAQRB0aqew==', '2024-01', 10); -- 4
-- pan: 1701111111111111
insert into card (pan, security_code, expiration_date, available_funds)
values ('NtUx6LpBHxWRvIaHzoctcAU/oNR80zbW5zMUQm6Ld2Q=|aGVYs8gy+GJQIEa/r3DOJA==', '3jiq7E30G9ouTkPdt5Zcww==|D5B9oxxyyktAJAQRB0aqew==', '2025-02', 0); -- 5
-- pan: 1701111222221111
insert into card (pan, security_code, expiration_date, available_funds)
values ('+e4BYpd5nd6AoapN7fOKUcPS+gcADe1UPXKxx/s29n4=|FipDfoiO+ZET2crzGGjwxw==', '3jiq7E30G9ouTkPdt5Zcww==|D5B9oxxyyktAJAQRB0aqew==', '2023-12', 0); -- 6

insert into card_owner (type, merchant_id, merchant_email, password, bank_id, card_id)
values ('Merchant', 'jlSuwpyB4v62M7KBc4dXiXuwSg53AsnKtWV/zP9E0cA=|zj3rWlhdDkkqWcfAf9hMfw==', 'sb-nsr1z4072854@business.example.com', '4bgphCgirDKF3SYl4LvFRQ==|YA7u//HlmBJoA2EevRphGw==', 1, 2);
-- 111111111111111111111111111111
-- Merchant123!
-- insert into card_owner (type, name, merchant_id, merchant_email, password, success_url, failed_url, error_url, bank_id, card_id)
-- values ('Merchant', 'Vulkan', 'jlSuwpyB4v62M7KBc4dXiXuwSg53AsnKtWV/zP9E0cA=|zj3rWlhdDkkqWcfAf9hMfw==', 'sb-nsr1z4072854@business.example.com', '4bgphCgirDKF3SYl4LvFRQ==|YA7u//HlmBJoA2EevRphGw==', 'https://localhost:3000/success', 'https://localhost:3000/failed', 'https://localhost:3000/error', 1, 2);

insert into card_owner (type, first_name, last_name, email, nin, bank_id, card_id) values ('Client', 'Bojana', 'Kliska', 'reader1@gmail.com', '1245886359863', 1, 1);
insert into card_owner (type, first_name, last_name, email, nin, bank_id, card_id) values ('Client', 'Milan', 'Milanovic', 'milanm@maildrop.cc', '1245887759863', 2, 3);
