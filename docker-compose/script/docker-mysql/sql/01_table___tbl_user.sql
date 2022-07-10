CREATE TABLE IF NOT EXISTS tbl_user
(
    -- primary key
    id                  BIGINT UNSIGNED                 NOT NULL AUTO_INCREMENT PRIMARY KEY,

    -- columns
    client_id           VARCHAR(30)                     NOT NULL,
    nickname            VARCHAR(100)                        NULL,
    role                VARCHAR(16)                     NOT NULL,

    -- fk columns

    -- common columns
    deleted             CHAR            DEFAULT 'N'     NOT NULL,
    created_at          DATETIME        DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at          DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    deleted_at          DATETIME                            NULL,

    -- constraints
    CONSTRAINT tbl_user_client_id_uindex UNIQUE KEY (client_id),
    CONSTRAINT tbl_user_nickname_uindex UNIQUE KEY (nickname)

    -- common fk columns

    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci
    COMMENT = 'user table';