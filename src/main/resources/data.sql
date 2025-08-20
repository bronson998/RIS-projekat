-- Roles
INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_USER');

-- Users
INSERT INTO users (username, password, email)
VALUES ('admin', 'admin123', 'admin@example.com');
INSERT INTO users (username, password, email)
VALUES ('branko', 'branko123', 'branko@example.com');

-- User roles (many-to-many)
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1); -- admin has ROLE_ADMIN
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2); -- john has ROLE_USER

-- Categories
INSERT INTO categories (name) VALUES ('Electronics');
INSERT INTO categories (name) VALUES ('Books');

-- Products
INSERT INTO products (name, description, price, quantity, category_id)
VALUES ('Laptop', 'Powerful gaming laptop', 1200.00, 5, 1);
INSERT INTO products (name, description, price, quantity, category_id)
VALUES ('Headphones', 'Noise cancelling headphones', 150.00, 10, 1);
INSERT INTO products (name, description, price, quantity, category_id)
VALUES ('Book: Spring Boot Basics', 'Learn Spring Boot', 25.00, 50, 2);

-- Cart for user branko
INSERT INTO carts (user_id, created_at) VALUES (2, NOW());

-- Cart items
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (1, 1, 1);
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (1, 3, 2);

-- Order for branko
INSERT INTO orders (user_id, status, total_price, created_at)
VALUES (2, 'PENDING', 1250.00, NOW());

-- Order items
INSERT INTO order_items (order_id, product_id, quantity, price)
VALUES (1, 1, 1, 1200.00);
INSERT INTO order_items (order_id, product_id, quantity, price)
VALUES (1, 2, 1, 50.00);

-- Wishlist for branko
INSERT INTO wishlists (user_id, name) VALUES (2, 'Letnja kupovina');

-- Wishlist items
INSERT INTO wishlist_items (wishlist_id, product_id) VALUES (1, 2);

-- Message from admin to branko
INSERT INTO messages (sender_id, receiver_id, content, timestamp)
VALUES (1, 2, 'Welcome to the shop, Branko!', NOW());