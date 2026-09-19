-- =========================================================
-- Categories
-- =========================================================

INSERT INTO category (id, description, name)
VALUES
    (1, 'Electronic devices and accessories', 'Electronics'),
    (2, 'Computers and laptops', 'Computers'),
    (3, 'Mobile phones and accessories', 'Smartphones');


-- =========================================================
-- Products
-- =========================================================

INSERT INTO product (
    id,
    description,
    name,
    available_quantity,
    price,
    category_id
)
VALUES
    (1, 'Wireless Bluetooth headphones', 'Sony WH-1000XM5', 15, 3499.00, 1),
    (2, '15-inch laptop with Intel processor', 'HP Pavilion 15', 10, 7299.00, 2),
    (3, 'Android smartphone with 128GB storage', 'Samsung Galaxy A55', 20, 3999.00, 3),
    (4, 'Mechanical keyboard with RGB lighting', 'Logitech G Pro Keyboard', 8, 1199.00, 1),
    (5, 'Wireless mouse for computers', 'Logitech MX Master 3S', 25, 899.00, 2);