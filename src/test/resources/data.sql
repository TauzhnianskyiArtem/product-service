INSERT INTO currency (value)
VALUES ('USD'),
       ('EUR');

INSERT INTO price (value, currency_id)
VALUES (100.10, (SELECT id FROM currency WHERE value = 'USD')),
       (200.00, (SELECT id FROM currency WHERE value = 'USD')),
       (250.00, (SELECT id FROM currency WHERE value = 'USD'));

INSERT INTO product (name, active, price_id)
VALUES ('Product for Update', true, (SELECT id FROM price WHERE value = 100.10)),
        ('Test product 2', true, (SELECT id FROM price WHERE value = 200.00)),
        ('Test product 3', true, (SELECT id FROM price WHERE value = 250.00));
