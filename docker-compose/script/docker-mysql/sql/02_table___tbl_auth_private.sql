CREATE TABLE IF NOT EXISTS tbl_auth_private
(
    -- primary key
    id                      BIGINT UNSIGNED                                                             NOT NULL AUTO_INCREMENT PRIMARY KEY,

    -- fk columns
    user_id                 BIGINT UNSIGNED                                                             NOT NULL COMMENT 'tbl_user.id',

    -- columns
    email                   VARCHAR(100)                                                                    NULL COMMENT 'User email',
    password                VARCHAR(255)                                                                    NULL COMMENT 'password',
    provider                VARCHAR(255)                                                                    NULL COMMENT 'PASSWORD / OAUTH_GOOGLE / WALLET',
    email_verified           VARCHAR(4) DEFAULT 'N'                                                          NULL COMMENT 'Email verification' ,


    -- common columns
    version                 BIGINT UNSIGNED DEFAULT '0'                                                 NOT NULL,
    deleted                 CHAR            DEFAULT 'N'                                                 NOT NULL,
    created_at              DATETIME        DEFAULT CURRENT_TIMESTAMP                                   NOT NULL,
    updated_at              DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP       NOT NULL,
    deleted_at              DATETIME                                                                        NULL,
    created_by              BIGINT UNSIGNED                                                                 NULL,
    updated_by              BIGINT UNSIGNED                                                                 NULL,
    deleted_by              BIGINT UNSIGNED                                                                 NULL,

    -- constraints
    INDEX tbl_auth_private_user_id_index (user_id),
    CONSTRAINT tbl_auth_private_uindex UNIQUE KEY (user_id, provider),

    -- common fk columns
    FOREIGN KEY (created_by) REFERENCES tbl_user(id),
    FOREIGN KEY (updated_by) REFERENCES tbl_user(id),
    FOREIGN KEY (deleted_by) REFERENCES tbl_user(id)

    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci
    COMMENT = 'user auth private table';