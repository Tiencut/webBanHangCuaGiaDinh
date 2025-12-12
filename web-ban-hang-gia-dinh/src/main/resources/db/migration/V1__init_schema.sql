-- Initial schema for web-ban-hang-gia-dinh
CREATE TABLE IF NOT EXISTS users (
	id BIGSERIAL PRIMARY KEY,
	username VARCHAR(100) NOT NULL UNIQUE,
	password VARCHAR(200) NOT NULL,
	full_name VARCHAR(200),
	email VARCHAR(200),
	created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS categories (
	id BIGSERIAL PRIMARY KEY,
	code VARCHAR(100) NOT NULL UNIQUE,
	name VARCHAR(200) NOT NULL,
	description TEXT,
	is_active BOOLEAN DEFAULT true
);

CREATE TABLE IF NOT EXISTS products (
	id BIGSERIAL PRIMARY KEY,
	code VARCHAR(100) NOT NULL UNIQUE,
	name VARCHAR(300) NOT NULL,
	description TEXT,
	category_id BIGINT REFERENCES categories(id),
	base_price NUMERIC(19,2),
	selling_price NUMERIC(19,2),
	created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS customers (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(300),
	phone VARCHAR(50),
	email VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS orders (
	id BIGSERIAL PRIMARY KEY,
	customer_id BIGINT REFERENCES customers(id),
	total_amount NUMERIC(19,2),
	status VARCHAR(50),
	created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS order_items (
	id BIGSERIAL PRIMARY KEY,
	order_id BIGINT REFERENCES orders(id) ON DELETE CASCADE,
	product_id BIGINT REFERENCES products(id),
	quantity INT,
	unit_price NUMERIC(19,2)
);

