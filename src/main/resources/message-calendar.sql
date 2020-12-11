-- DROP TABLE public.user CASCADE;
-- DROP TABLE message CASCADE;
-- DROP TABLE user_message CASCADE;
-- DROP TABLE user_calendar CASCADE;
-- DROP TABLE calendar CASCADE;

-- CREATE TABLE public.user (
-- 	id SERIAL PRIMARY KEY,
-- 	login VARCHAR(30) NOT NULL,
-- 	password VARCHAR(30) NOT NULL
-- );

-- CREATE TABLE calendar (
-- 	id SERIAL PRIMARY KEY,
-- 	begin DATE,
-- 	finish DATE,
-- 	subject VARCHAR(50),
-- 	description TEXT,
-- 	attendees VARCHAR(256)
-- );

-- CREATE TABLE message (
-- 	id SERIAL PRIMARY KEY,
-- 	payload TEXT NOT NULL,
-- 	coming_from VARCHAR(30) NOT NULL,
-- 	calendar_id INTEGER NOT NULL,
-- 	FOREIGN KEY (calendar_id) REFERENCES calendar(id)
-- );

-- CREATE TABLE user_message (
-- 	id_user INTEGER NOT NULL,
-- 	id_message INTEGER NOT NULL,
-- 	FOREIGN KEY (id_user) REFERENCES public.user(id),
-- 	FOREIGN KEY (id_message) REFERENCES message(id)
-- );

-- CREATE TABLE user_calendar (
-- 	id_user INTEGER NOT NULL,
-- 	id_calendar INTEGER NOT NULL,
-- 	FOREIGN KEY (id_user) REFERENCES public.user(id),
-- 	FOREIGN KEY (id_calendar) REFERENCES calendar(id)
-- )

-- INSERT INTO public.user(login,password) VALUES
-- ('ronald', 'rd123'),
-- ('jean-robert', 'jr123');

-- INSERT INTO calendar(begin,finish,subject,description) VALUES
-- ('2020-11-28','2020-11-29','prendre RDV','ceci est un évènement. n°1'),
-- ('1985-07-01','1985-07-01','jour de naissance','desription de évènement. n°2'),
-- ('2015-01-01','2015-12-31','une année complète','desription de évènement. n°3');

-- INSERT INTO message(payload,coming_from,calendar_id) VALUES
-- ('Q09OR1JBVFVMQVRJT05TDQpjZWNpIGVzdCB1bmUgbWVzc2FnZSB0ZXN0DQpkb250IGxlIGZvcm1hdCBlc3QgZW5jb2TDqSBlbiBiYXNlNjQ', 'ron.desouter@gmail.com', 5),
-- ('TG9yZW0gaXBzdW0gZG9sb3Igc2l0IGFtZXQsIGNvbnNlY3RldHVyIGFkaXBpc2NpbmcgZWxpdC4gRnVzY2UgcGxhY2VyYXQgcHVydXMgbGVvLCANCg0KdXQgZGlnbmlzc2ltIGxlY3R1cyBlZmZpY2l0dXIgdml0YWUuIENyYXMgZXVpc21vZCBkb2xvciBlZ2V0IGxpYmVybyBtYXhpbXVzLCANCmlkIGJpYmVuZHVtIG1hdXJpcyBhbGlxdWV0LiBQaGFzZWxsdXMgY29uc2VjdGV0dXIgdGVsbHVzIHF1aXMgZGlhbSBlbGVpZmVuZCBzY2VsZXJpc3F1ZS4NCkNyYXMgcGxhY2VyYXQgcG9ydHRpdG9yIGZlbGlzIGFjIGltcGVyZGlldC4gTWFlY2VuYXMgbWFzc2EgZXJvcywgcGVsbGVudGVzcXVlIGluIHBoYXJldHJhIGEsIHJ1dHJ1bSBhdCBkb2xvci4', 'jean.robert@gmail.com', 4),
-- ('U2FsdXRhdGlvbnMgZGlzdGluZ3XDqWVzDQoNClLDqXN1bcOpIDogcmVuZGV6LXZvdXMgcHJpcyB2aWEgbCdBUEkNCkFkcmVzc2UgOiBydWUgZHUgdG91Y2FuLCAxMzUwIE5vZHV3ZXosIEJlbGdpcXVlDQoNClTDqWzDqXBob25lIDogMDQ4NC43MjYuNDc5DQpEYXRlIGRlIGTDqWJ1dCA6IDIwLjExLjIwMjANCkRhdGUgZGUgZmluIDogMjEuMTEuMjAyMA0KDQpGaW4gZHUgbWVzc2FnZSBxdWkgbidlc3QgcGFzIHByaXMgZW4gY29tcHRlDQoNCkNkbHQNClJvbmFsZCBEZSBTb3V0ZXI', 'ron.desouter@gmail.com', 6);

-- INSERT INTO user_message(id_user, id_message) VALUES
-- (3,2),
-- (3,4),
-- (4,3);


--SELECT public.user.login, public.user.password, message.*, calendar.* FROM user_message
--INNER JOIN public.user ON public.user.id = user_message.id_user
--INNER JOIN message ON message.id = user_message.id_message
--INNER JOIN calendar ON calendar.id = message.calendar_id
--ORDER BY public.user.login ASC;

-- SELECT * FROM calendar
-- INNER JOIN message ON message.calendar_id = calendar.id;
