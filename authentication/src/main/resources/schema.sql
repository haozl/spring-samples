DROP TABLE users IF EXISTS;
CREATE TABLE users (
  id       INTEGER PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(32) UNIQUE NOT NULL,
  password VARCHAR(64)        NOT NULL,
  enabled  TINYINT            NOT NULL DEFAULT 1,
  email    VARCHAR(32) UNIQUE
);

DROP TABLE user_roles IF EXISTS;
CREATE TABLE user_roles (
  id      INTEGER PRIMARY KEY AUTO_INCREMENT,
  user_id INTEGER     NOT NULL,
  role    VARCHAR(48) NOT NULL,
  UNIQUE KEY role_user( role, user_id)
);



