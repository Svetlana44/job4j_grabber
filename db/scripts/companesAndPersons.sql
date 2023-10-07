create table company
(
    id integer not null,
    name character varying,
    constraint company_pkey primary key (id)
);
create table person
(
    id integer not null,
    name character varying,
    company_id integer references company(id),
    constraint person_pkey primary key (id)
);

insert into company(id,name) values(1,'name1'),
								   (2,'name2'),
								   (3,'name3'),
								   (4,'name4'),
								   (5,'name5'),
								   (6,'name6');

insert into person(id,name,company_id) values
											(1,'man1',1),
								   			(2,'man2',2),
										    (3,'man3',5),
										    (4,'man4',1),
										    (5,'man5',5),
										    (6,'man6',4);


select p.name,(select c.name from company c where c.id=p.company_id) cname
from person p where company_id<>5;

select count(name),company_id  from person p group by  company_id
having count(name)>=(
select max(counts) from (
		select count(name) counts from person p group by  company_id) t);