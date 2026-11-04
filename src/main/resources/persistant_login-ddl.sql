-- Remember-me token store used by JdbcTokenRepositoryImpl (executed on startup via spring.sql.init)
CREATE TABLE IF NOT EXISTS persistent_logins
(
    username  varchar(64) NOT NULL,
    series    varchar(64) PRIMARY KEY,
    token     varchar(64) NOT NULL,
    last_used timestamp   NOT NULL
);
