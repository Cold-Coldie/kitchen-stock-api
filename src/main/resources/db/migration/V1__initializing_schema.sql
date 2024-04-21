CREATE TABLE USERS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    country VARCHAR(255),
    currency VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    role ENUM('ADMIN', 'USER') NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    CONSTRAINT UC_EMAIL UNIQUE (email)
);

CREATE TABLE MEASURING_UNITS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES USERS(id)
);


CREATE TABLE ITEMS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    low_limit INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    currency_name VARCHAR(255) NOT NULL,
    currency_symbol VARCHAR(5) NOT NULL,
    need_restock TINYINT(1),
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    user_id BIGINT,
    measuring_unit_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES USERS(id),
    FOREIGN KEY (measuring_unit_id) REFERENCES MEASURING_UNITS(id)
);

CREATE TABLE MARKET_LISTS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    low_limit INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    currency_name VARCHAR(255) NOT NULL,
    currency_symbol VARCHAR(10) NOT NULL,
    need_restock BOOLEAN,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME,
    user_id BIGINT,
    measuring_unit_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES USERS(id),
    FOREIGN KEY (measuring_unit_id) REFERENCES MEASURING_UNITS(id)
);

CREATE TABLE TOKENS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    token_type ENUM('BEARER') NOT NULL,
    expired BOOLEAN,
    revoked BOOLEAN,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES Users(id)
);
