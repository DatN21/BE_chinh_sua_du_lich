ALTER TABLE booking ADD COLUMN payment_id INT;
ALTER TABLE booking 
ADD CONSTRAINT fk_payment_id
FOREIGN KEY (payment_id) REFERENCES payment(id)
ON DELETE CASCADE;  -- Bạn có thể chọn hành động phù hợp như CASCADE hoặc SET NULL


ALTER TABLE payment
ADD COLUMN booking_id INT;

ALTER TABLE payment
ADD CONSTRAINT fk_payment_booking
FOREIGN KEY (booking_id)
REFERENCES booking(id)
ON DELETE CASCADE;

ALTER TABLE users
DROP FOREIGN KEY users_ibfk_1;

CREATE TABLE user_roles (
  user_id BIGINT,
  role_id BIGINT,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (role_id) REFERENCES roles(id)
);


