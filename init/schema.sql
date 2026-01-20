CREATE SEQUENCE payments_seq START 1;

CREATE TABLE users (
                       id BIGINT PRIMARY KEY,
                       full_name VARCHAR(100) NOT NULL,
                       balance DECIMAL(15,2) NOT NULL
);

CREATE TABLE payments (
                          id BIGINT PRIMARY KEY,
                          user_id BIGINT NOT NULL,
                          amount DECIMAL(15,2) NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          created_at TIMESTAMP NOT NULL,
                          CONSTRAINT fk_payments_user
                              FOREIGN KEY (user_id) REFERENCES users(id)
);
