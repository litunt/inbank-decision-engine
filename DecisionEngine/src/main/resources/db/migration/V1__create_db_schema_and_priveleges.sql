CREATE SCHEMA IF NOT EXISTS core;

CREATE ROLE readonly;

CREATE ROLE engine_user WITH
    LOGIN
    PASSWORD 'root'
    NOSUPERUSER
    NOCREATEDB
    CONNECTION LIMIT -1;

GRANT CONNECT ON DATABASE decisionengine TO readonly;

GRANT readonly TO engine_user;
