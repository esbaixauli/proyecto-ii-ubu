create database cbr;

create table usuario(
	id integer primary key,
	nombre varchar(20) unique,
	password varchar(20),
	tipo char(2) check tipo in('UB','UA','A')
);

create table tecnica(
	id integer primary key,
	nombre varchar(20) unique,
	tipo char(3) check tipo in('rec','reu','rev','ret')
);

create table caso(
	id integer primary key,
	nombre varchar(20) unique,
	recPorDefecto integer not null references tecnica,
	reuPorDefecto integer not null references tecnica,
	revPorDefecto integer not null references tecnica,
	retPorDefecto integer not null references tecnica,
);

create table caso_tecnica_rec(
	id_caso integer references caso,
	id_tecnica integer references tecnica,
	primary key(id_caso, id_tecnica)
);


create table caso_tecnica_reu(
	id_caso integer references caso,
	id_tecnica integer references tecnica,
	primary key(id_caso, id_tecnica)
);


create table caso_tecnica_rev(
	id_caso integer references caso,
	id_tecnica integer references tecnica,
	primary key(id_caso, id_tecnica)
);


create table caso_tecnica_ret(
	id_caso integer references caso,
	id_tecnica integer references tecnica,
	primary key(id_caso, id_tecnica)
);




create table caso_usuario(
	id_caso integer references caso,
	id_usuario integer references usuario,
	primary key(id_caso, id_usuario)
);

create table atributo(
	nombre varchar(20),
	tipo varchar(20),
	peso decimal,
	metrica varchar(20),
	caso integer references caso,
	primary key (nombre,caso)
);


insert into tecnica values(0,'K-NN','rec');
insert into tecnica values(1,'Umbral','rec');

