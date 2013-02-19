create table usuario(
	id integer GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) primary key,
	nombre varchar(20) unique,
	password varchar(20),
	tipo char(2),
	check (tipo in ('UB','UA','A'))
);

create table tecnica(
	id integer GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) primary key,
	nombre varchar(20) unique,
	tipo char(3),
	check (tipo in ('rec','reu','rev','ret'))
);

create table caso(
	id integer GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) primary key,
	nombre varchar(20) unique,
	defaultRec integer references tecnica,
	defaultReu integer references tecnica,
	defaultRev integer references tecnica,
	defaultRet integer references tecnica
);

create table parametro(
	id integer GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) primary key,
	nombre varchar(20),
	valor number
);

create table caso_tecnica_parametro(
	id_caso integer references caso,
	id_tecnica integer references tecnica,
	id_parametro integer references parametro,
	primary key(id_caso, id_tecnica, id_parametro)
);

create table caso_usuario(
	id_caso integer references caso,
	id_usuario integer references usuario,
	primary key(id_caso, id_usuario)
);

create table atributo(
	nombre varchar(20),
	tipo char(1),
	peso decimal,
	metrica varchar(20),
	parammetrica decimal,
	caso integer references caso on delete cascade,
	problema boolean,
	primary key (caso,nombre),
	check (tipo in ('S', 'I', 'D')),
);

insert into tecnica(nombre, tipo) values('K-NN','rec');
insert into tecnica(nombre, tipo) values('Umbral','rec');

insert into usuario(nombre, password, tipo) values('root','root','A');
