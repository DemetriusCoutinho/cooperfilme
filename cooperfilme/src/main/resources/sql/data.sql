insert into usuario(id,name,email,password) values (1,'Analista','analista@cooperfilme.com','analista@123');
insert into usuario(id,name,email,password) values (2,'Revisor','revisor@cooperfilme.com','revisor@123');
insert into usuario(id,name,email,password) values (3,'Aprovador1','aprovador1@cooperfilme.com','aprovador1@123');
insert into usuario(id,name,email,password) values (4,'Aprovador2','aprovador2@cooperfilme.com','aprovador2@123');
insert into usuario(id,name,email,password) values (5,'Aprovador3','aprovador3@cooperfilme.com','aprovador3@123');

insert into role(name,id_user) values('ANALISTA',1);
insert into role(name,id_user) values('REVISOR',2);
insert into role(name,id_user) values('APROVADOR',3);
insert into role(name,id_user) values('APROVADOR',4);
insert into role(name,id_user) values('APROVADOR',5);