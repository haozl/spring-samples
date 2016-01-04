-- password 123
insert into users(username,password,enabled,email) VALUES ('sa','$2a$10$A2NTRnkuPrYY.rwbNZADuOfFYk.NIzqanJ2KgxeKJFaniMGdjp7lm',true,'sa@gmail.com');
insert into users(username,password,enabled,email) VALUES ('tom','$2a$10$wZzaPbR2TkN0.LKNjrke6.M80N12olMaV4T4Cjgg798D.HV85phTW',true,'tom@gmail.com');
insert into users(username,password,enabled,email) VALUES ('john','$2a$10$TLj06.AIpryUEHZl48BgUOc1vUwUvisRYIiF03gcK3bVCGIZAsmKy',true,'john@gmail.com');


insert into user_roles(user_id,role) VALUES (1,'ROLE_USER');
insert into user_roles(user_id,role) VALUES (1,'ROLE_ADMIN');
insert into user_roles(user_id,role) VALUES (2,'ROLE_USER');
