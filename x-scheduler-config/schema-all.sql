--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.13
-- Dumped by pg_dump version 9.1.13
-- Started on 2015-02-05 05:40:29 WIB

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 2765 (class 1262 OID 434794)
-- Name: xwfs; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE xwfs WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_US.UTF-8' LC_CTYPE = 'en_US.UTF-8';


\connect xwfs

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 167 (class 3079 OID 12506)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2768 (class 0 OID 0)
-- Dependencies: 167
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 166 (class 1259 OID 434849)
-- Dependencies: 5
-- Name: activity_log; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE activity_log (
    id character varying(255) NOT NULL,
    created_by character varying(255),
    created_date timestamp without time zone NOT NULL,
    mark_for_delete boolean,
    store_id character varying(255),
    updated_by character varying(255),
    updated_date timestamp without time zone,
    optlock bigint,
    log_date timestamp without time zone NOT NULL,
    description character varying(255),
    job_class character varying(255) NOT NULL,
    name character varying(255)
);


--
-- TOC entry 162 (class 1259 OID 434811)
-- Dependencies: 2637 5
-- Name: command; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE command (
    id character varying(255) NOT NULL,
    created_by character varying(255),
    created_date timestamp without time zone NOT NULL,
    mark_for_delete boolean,
    store_id character varying(255),
    updated_by character varying(255),
    updated_date timestamp without time zone,
    optlock bigint,
    command character varying(255) NOT NULL,
    parameters character varying(255),
    command_type character varying(255),
    contents character varying(255),
    entry_point character varying(255),
    CONSTRAINT command_command_type_check CHECK (((command_type)::text = ANY ((ARRAY['WEB_SERVICE'::character varying, 'CLIENT_SDK'::character varying, 'SHELL_SCRIPT'::character varying])::text[])))
);


--
-- TOC entry 161 (class 1259 OID 434795)
-- Dependencies: 5
-- Name: task; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE task (
    id character varying(255) NOT NULL,
    created_by character varying(255),
    created_date timestamp without time zone NOT NULL,
    mark_for_delete boolean,
    store_id character varying(255),
    updated_by character varying(255),
    updated_date timestamp without time zone,
    optlock bigint,
    task_name character varying(55) NOT NULL,
    command_id character varying(255) NOT NULL
);


--
-- TOC entry 163 (class 1259 OID 434825)
-- Dependencies: 5
-- Name: workflow; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE workflow (
    id character varying(255) NOT NULL,
    created_by character varying(255),
    created_date timestamp without time zone NOT NULL,
    mark_for_delete boolean,
    store_id character varying(255),
    updated_by character varying(255),
    updated_date timestamp without time zone,
    optlock bigint,
    workflow_name character varying(55) NOT NULL
);


--
-- TOC entry 164 (class 1259 OID 434833)
-- Dependencies: 5
-- Name: workflow_schedule; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE workflow_schedule (
    id character varying(255) NOT NULL,
    created_by character varying(255),
    created_date timestamp without time zone NOT NULL,
    mark_for_delete boolean,
    store_id character varying(255),
    updated_by character varying(255),
    updated_date timestamp without time zone,
    optlock bigint,
    day_of_month integer,
    day_of_week integer,
    hours integer,
    minutes integer,
    months integer,
    workflow_id character varying(255)
);


--
-- TOC entry 165 (class 1259 OID 434841)
-- Dependencies: 5
-- Name: workflow_tasks; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE workflow_tasks (
    workflow_id character varying(255) NOT NULL,
    task_id character varying(255) NOT NULL
);


--
-- TOC entry 2760 (class 0 OID 434849)
-- Dependencies: 166 2761
-- Data for Name: activity_log; Type: TABLE DATA; Schema: public; Owner: -
--

COPY activity_log (id, created_by, created_date, mark_for_delete, store_id, updated_by, updated_date, optlock, log_date, description, job_class, name) FROM stdin;
\.


--
-- TOC entry 2756 (class 0 OID 434811)
-- Dependencies: 162 2761
-- Data for Name: command; Type: TABLE DATA; Schema: public; Owner: -
--

COPY command (id, created_by, created_date, mark_for_delete, store_id, updated_by, updated_date, optlock, command, parameters, command_type, contents, entry_point) FROM stdin;
1	tux	2015-01-01 00:00:00	f	store-123	tux	2015-01-01 00:00:00	1	{"url":"http://www.google.com","method":"GET"}		WEB_SERVICE		\N
76656b64-3bac-47af-b479-bda9c9eb694f	system	2015-02-02 11:00:55.076	f	\N	system	2015-02-02 11:00:55.076	0	{"url":"http://md5.jsontest.com/","method":"GET"}	text=yauritux	WEB_SERVICE		\N
b657260a-c92a-4721-ab36-afbe43a8bc02	system	2015-02-03 14:07:57.642	f	\N	system	2015-02-03 14:07:57.642	0	{"url":"https://www.google.com","method":"GET"}		WEB_SERVICE		\N
\.


--
-- TOC entry 2755 (class 0 OID 434795)
-- Dependencies: 161 2761
-- Data for Name: task; Type: TABLE DATA; Schema: public; Owner: -
--

COPY task (id, created_by, created_date, mark_for_delete, store_id, updated_by, updated_date, optlock, task_name, command_id) FROM stdin;
ac4fc20b-824d-48fc-99d7-9694d0e73be1	system	2015-02-03 09:08:48.182	f	store-123	system	2015-02-03 09:08:48.182	0	MD5 Generator	76656b64-3bac-47af-b479-bda9c9eb694f
8037b6ba-0d12-4a45-a03c-36585429a48e	system	2015-02-03 14:10:42.047	f	store-123	system	2015-02-03 14:10:42.047	0	xxx	b657260a-c92a-4721-ab36-afbe43a8bc02
\.


--
-- TOC entry 2757 (class 0 OID 434825)
-- Dependencies: 163 2761
-- Data for Name: workflow; Type: TABLE DATA; Schema: public; Owner: -
--

COPY workflow (id, created_by, created_date, mark_for_delete, store_id, updated_by, updated_date, optlock, workflow_name) FROM stdin;
\.


--
-- TOC entry 2758 (class 0 OID 434833)
-- Dependencies: 164 2761
-- Data for Name: workflow_schedule; Type: TABLE DATA; Schema: public; Owner: -
--

COPY workflow_schedule (id, created_by, created_date, mark_for_delete, store_id, updated_by, updated_date, optlock, day_of_month, day_of_week, hours, minutes, months, workflow_id) FROM stdin;
\.


--
-- TOC entry 2759 (class 0 OID 434841)
-- Dependencies: 165 2761
-- Data for Name: workflow_tasks; Type: TABLE DATA; Schema: public; Owner: -
--

COPY workflow_tasks (workflow_id, task_id) FROM stdin;
\.


--
-- TOC entry 2649 (class 2606 OID 434856)
-- Dependencies: 166 166 2762
-- Name: activity_log_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY activity_log
    ADD CONSTRAINT activity_log_pkey PRIMARY KEY (id);


--
-- TOC entry 2641 (class 2606 OID 434819)
-- Dependencies: 162 162 2762
-- Name: command_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY command
    ADD CONSTRAINT command_pkey PRIMARY KEY (id);


--
-- TOC entry 2639 (class 2606 OID 434802)
-- Dependencies: 161 161 2762
-- Name: task_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY task
    ADD CONSTRAINT task_pkey PRIMARY KEY (id);


--
-- TOC entry 2643 (class 2606 OID 434832)
-- Dependencies: 163 163 2762
-- Name: workflow_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY workflow
    ADD CONSTRAINT workflow_pkey PRIMARY KEY (id);


--
-- TOC entry 2645 (class 2606 OID 434840)
-- Dependencies: 164 164 2762
-- Name: workflow_schedule_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
-- ALTER TABLE ONLY workflow_schedule ADD CONSTRAINT workflow_schedule_pkey PRIMARY KEY (id);


--
-- TOC entry 2647 (class 2606 OID 434848)
-- Dependencies: 165 165 165 2762
-- Name: workflow_tasks_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY workflow_tasks
    ADD CONSTRAINT workflow_tasks_pkey PRIMARY KEY (workflow_id, task_id);


--
-- TOC entry 2653 (class 2606 OID 434867)
-- Dependencies: 163 2642 165 2762
-- Name: fk_bawikoiw1k0bil1bvwq5qpa0j; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY workflow_tasks
    ADD CONSTRAINT fk_bawikoiw1k0bil1bvwq5qpa0j FOREIGN KEY (workflow_id) REFERENCES workflow(id);


--
-- TOC entry 2652 (class 2606 OID 434862)
-- Dependencies: 165 161 2638 2762
-- Name: fk_kab4f28ri1qxbk2ssmf9kh8em; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY workflow_tasks
    ADD CONSTRAINT fk_kab4f28ri1qxbk2ssmf9kh8em FOREIGN KEY (task_id) REFERENCES task(id);


--
-- TOC entry 2651 (class 2606 OID 434857)
-- Dependencies: 2642 163 164 2762
-- Name: fk_l2xr6mc80blxapgo5d4kncf8d; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY workflow_schedule
    ADD CONSTRAINT fk_l2xr6mc80blxapgo5d4kncf8d FOREIGN KEY (workflow_id) REFERENCES workflow(id);


--
-- TOC entry 2650 (class 2606 OID 434820)
-- Dependencies: 2640 162 161 2762
-- Name: task_command_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY task
    ADD CONSTRAINT task_command_id_fkey FOREIGN KEY (command_id) REFERENCES command(id);


--
-- TOC entry 2767 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: -
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-02-05 05:40:34 WIB

--
-- PostgreSQL database dump complete
--

