ALTER TABLE customers
ADD COLUMN parent_id BIGINT NULL,
ADD CONSTRAINT fk_customer_parent
  FOREIGN KEY (parent_id) REFERENCES customers(id)
  ON DELETE SET NULL;
