insert into app (app_id, app_name, official_email, web_address)
values ('43d86eb5-b5e9-46db-ae03-af4a84350770', 'Literary Association', 'literaryassociation7@gmail.com', 'https://literaryassociation.com');

insert into merchant (merchant_name, merchant_email, merchant_id, merchant_password, password, activated, error_url, failed_url, success_url, app_id)
values ('Vulkan knjizare', 'sb-nsr1z4072854@business.example.com','cVnsMpdb6OIwN8y9yPkVqVScGi1q5RGKh8nsmZjUgsk=|8bAC7Y7K5L8cDdoBhj2Erw==', 'eqRcB55gBHQwhvVZpnqxwg==|zZ/AAJw9MZUDdfXbipfbDw==', 'neki_password', true, 'https://localhost:3000/error', 'https://localhost:3000/failed', 'https://localhost:3000/success', 1);

insert into payment_method (name, deleted) values ('Bank', false);
insert into payment_method (name, deleted) values ('PayPal', false);
insert into payment_method (name, deleted) values ('Bitcoin', false);

insert into apps_payment_methods (app_id, payment_method_id) values (1,1);
insert into apps_payment_methods (app_id, payment_method_id) values (1,2);
insert into apps_payment_methods (app_id, payment_method_id) values (1,3);

insert into merchants_payment_methods (merchant_id, payment_method_id) values (1,1);
insert into merchants_payment_methods (merchant_id, payment_method_id) values (1,2);
insert into merchants_payment_methods (merchant_id, payment_method_id) values (1,3);

insert into billing_plan ( billing_plan_id, cycles_number, merchant_id, type, frequency )
values ( 'P-1A648881X5345862WL7ZRIDY', 2, 1, 'FIXED', 'MONTH');
