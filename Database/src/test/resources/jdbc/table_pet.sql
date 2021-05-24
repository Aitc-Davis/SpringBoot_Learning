CREATE TABLE pet (
	id varchar(20) NOT NULL,
	petname varchar(50) NULL,
	CONSTRAINT pet_pk PRIMARY KEY (id)
);

INSERT INTO pet
(id, petname)
VALUES('12345', 'CCCCat');
