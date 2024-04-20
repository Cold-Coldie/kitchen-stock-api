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