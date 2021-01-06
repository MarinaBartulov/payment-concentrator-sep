insert into app (app_id, app_name, official_email, web_address)
values ('43d86eb5-b5e9-46db-ae03-af4a84350770', 'Literary Association', 'literaryassociation7@gmail.com', 'https://literaryassociation.com');
insert into merchant (merchant_id, merchant_email, password, error_url, failed_url, success_url, app_id)
values ('46QfIZh9KGe62AMDAStgnRbsK1fcX4', 'sb-nsr1z4072854@business.example.com', 'Merchant123!', 'https://localhost:3000/error', 'https://localhost:3000/failed', 'https://localhost:3000/success', 1);

insert into payment_method (name, deleted) values ('Bank', false);
insert into payment_method (name, deleted) values ('PayPal', false);
insert into payment_method (name, deleted) values ('Bitcoin', false);

insert into apps_payment_methods (app_id, payment_method_id) values (1,1);
insert into apps_payment_methods (app_id, payment_method_id) values (1,2);
insert into apps_payment_methods (app_id, payment_method_id) values (1,3);

insert into merchants_payment_methods (merchant_id, payment_method_id) values (1,1);
insert into merchants_payment_methods (merchant_id, payment_method_id) values (1,2);
insert into merchants_payment_methods (merchant_id, payment_method_id) values (1,3);

