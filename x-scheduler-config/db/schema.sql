create table command(
 id character varying(255) not null,
 created_by character varying(255),
 created_date timestamp without time zone not null,
 mark_for_delete boolean,
 store_id character varying(255),
 updated_by character varying(255),
 updated_date timestamp without time zone,
 optlock bigint,
 command character varying(255) not null,
 parameters character varying(255),
 command_type character varying(100) check (command_type in ('WEB_SERVICE', 'CLIENT_SDK', 'SHELL_SCRIPT'))
);

alter table command add primary key(id);

create table task(
 id character varying(255) not null,
 created_by character varying(255),
 created_date timestamp without time zone not null,
 mark_for_delete boolean,
 store_id character_id varying(255),
 updated_by character varying(255),
 updated_date timestamp without time zone,
 optlock bigint,
 task_name character varying(255) not null,
 command_id character varying(255)
);

alter table task add primary key(id);

alter table task add foreign key(command_id) references command(id);
