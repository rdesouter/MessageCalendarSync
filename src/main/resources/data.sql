 DROP TABLE IF EXISTS public.user CASCADE;
 DROP TABLE IF EXISTS public.message CASCADE;
 DROP TABLE IF EXISTS public.user_message CASCADE;
 DROP TABLE IF EXISTS public.user_calendar CASCADE;
 DROP TABLE IF EXISTS public.calendar CASCADE;

 CREATE TABLE public.user (
 	id SERIAL PRIMARY KEY,
 	login VARCHAR(30) NOT NULL,
 	password VARCHAR(255) NOT NULL,
 	role VARCHAR(30) NOT NULL
 );

 CREATE TABLE public.calendar (
 	id SERIAL PRIMARY KEY,
 	begin DATE,
 	finish DATE,
 	subject VARCHAR(50),
 	description TEXT,
 	attendees VARCHAR(256)
 );

 CREATE TABLE public.message (
 	id SERIAL PRIMARY KEY,
 	payload TEXT NOT NULL,
 	coming_from VARCHAR(30) NOT NULL,
 	calendar_id INTEGER NOT NULL,
 	FOREIGN KEY (calendar_id) REFERENCES calendar(id)
 );

 CREATE TABLE public.user_message (
 	id_user INTEGER NOT NULL,
 	id_message INTEGER NOT NULL,
 	FOREIGN KEY (id_user) REFERENCES public.user(id),
 	FOREIGN KEY (id_message) REFERENCES message(id)
 );

 CREATE TABLE public.user_calendar (
 	id_user INTEGER NOT NULL,
 	id_calendar INTEGER NOT NULL,
 	FOREIGN KEY (id_user) REFERENCES public.user(id),
 	FOREIGN KEY (id_calendar) REFERENCES calendar(id)
 );

 INSERT INTO public.user(login,password, role) VALUES
 ('ronald', '$2a$10$rZiccs5oDID3oZyRxIKAkeWBm9jycmOc/QPXnTHPgVhxBmIT6BnVS', 'guest');
 INSERT INTO calendar(begin,finish,subject,description) VALUES
 ('2020-11-28','2020-11-29','prendre RDV','ceci est un évènement. n°1');
 INSERT INTO message(payload,coming_from,calendar_id) VALUES
 ('Q09OR1JBVFVMQVRJT05TDQpjZWNpIGVzdCB1bmUgbWVzc2FnZSB0ZXN0DQpkb250IGxlIGZvcm1hdCBlc3QgZW5jb2TDqSBlbiBiYXNlNjQ', 'ron.desouter@gmail.com', 1);
 INSERT INTO user_message(id_user, id_message) VALUES
 (1,1);