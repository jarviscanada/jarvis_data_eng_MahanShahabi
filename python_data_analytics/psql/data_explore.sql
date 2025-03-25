-- Show table schema
\d+ retail; 
-- Show first 10 rows 
SELECT * FROM retail LIMIT 10; 
-- Check # of records 
select count(*) from retail r; 
-- number of clients (e.g. unique client ID) 
select count(distinct customer_id) from retail r; 
-- invoice date range (e.g. max/min dates)
select max(invoice_date), min(invoice_date) from retail r;  
-- number of SKU/merchants (e.g. unique stock code)
select count(distinct stock_code) from retail r; 
-- Calculate average invoice amount excluding invoices with a negative amount (e.g. canceled orders have negative amount)
SELECT AVG(invoice_total) AS average_invoice_amount
FROM (
    SELECT invoice_no, SUM(unit_price * quantity) AS invoice_total
    FROM retail
    WHERE quantity > 0 
    GROUP BY invoice_no
) AS invoice_totals;
-- Calculate total revenue (e.g. sum of unit_price * quantity)
select sum(unit_price * quantity) from retail r; 
-- Calculate total revenue by YYYYMM 
SELECT to_char(invoice_date, 'yyyymm') as yyyymm, sum(unit_price * quantity) from retail r group by yyyymm order by yyyymm;