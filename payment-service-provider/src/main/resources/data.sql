insert into merchant (merchant_id, merchant_email, password, error_url, failed_url, success_url)
values ('46QfIZh9KGe62AMDAStgnRbsK1fcX4', 'sb-nsr1z4072854@business.example.com', 'Merchant123!', 'https://localhost:3000/error', 'https://localhost:3000/failed', 'https://localhost:3000/success');

insert into billing_plan ( billing_plan_id, cycles_number, merchant_id, type, frequency )
values ( 'P-1A648881X5345862WL7ZRIDY', 2, 1, 'FIXED', 'MONTH');
