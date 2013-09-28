-- Drop objects
DROP DATABASE shared_dev;
DROP DATABASE master_ci;
DROP DATABASE themis_qa;

DROP ROLE themis;

CREATE ROLE themis LOGIN
  PASSWORD 'themis'
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION CONNECTION LIMIT 200;

-- DATABASE: shared_dev
CREATE DATABASE shared_dev
  WITH ENCODING='UTF8'
       OWNER=themis_admin
       CONNECTION LIMIT=-1;

-- DATABASE: master_ci
CREATE DATABASE master_ci
  WITH ENCODING='UTF8'
       OWNER=themis_admin
       CONNECTION LIMIT=-1;

-- DATABASE: themis_qa
CREATE DATABASE themis_qa
  WITH ENCODING='UTF8'
       OWNER=themis_admin
       CONNECTION LIMIT=-1;

-- Schema: themis
\connect shared_dev
CREATE SCHEMA themis
  AUTHORIZATION themis;

-- Schema: themis
\connect themis_qa
CREATE SCHEMA themis
  AUTHORIZATION themis;

-- Schema: themis
\connect master_ci
CREATE SCHEMA themis
  AUTHORIZATION themis;
