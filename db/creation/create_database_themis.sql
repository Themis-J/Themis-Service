-- Drop objects
DROP DATABASE themis_dev;
DROP ROLE local_dev;
DROP ROLE themis_admin;

-- Role: themis_admin
CREATE ROLE themis_admin LOGIN
  ENCRYPTED PASSWORD 'md5e354ca7a0fd4615ac5aa3a88a58dc6a5'
  SUPERUSER INHERIT CREATEDB CREATEROLE REPLICATION;

-- Role: local_dev
CREATE ROLE local_dev LOGIN
  ENCRYPTED PASSWORD 'md5e52e215e21a0437c9cb22bbb61b598dc'
  SUPERUSER INHERIT CREATEDB CREATEROLE REPLICATION;

-- DATABASE: themis_dev
CREATE DATABASE themis_dev
  WITH ENCODING='UTF8'
       OWNER=themis_admin
       CONNECTION LIMIT=-1;

-- Schema: local_dev
\connect themis_dev
CREATE SCHEMA local_dev
  AUTHORIZATION local_dev;
