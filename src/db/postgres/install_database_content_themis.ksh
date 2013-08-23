#!/bin/ksh

DATABASE_INSTALL_PKG_PATH=/Users/chen386_2000/MyWorkspace/dealer/src/db/postgres/

DATABASE_USER=postgres
DATABASE_NAME=themis
DATABASE_SERVER=localhost

CREATE_ALL_TABLES_SQL_PATH=$DATABASE_INSTALL_PKG_PATH/create_themis_tables.sql
echo "Installing new tables..."
psql -h $DATABASE_SERVER -f $CREATE_ALL_TABLES_SQL_PATH -U $DATABASE_USER $DATABASE_NAME

INSERT_INIT_DATA_SQL_PATH=$DATABASE_INSTALL_PKG_PATH/insert_themis_data.sql
echo "Installing new table content..."
psql -h $DATABASE_SERVER -f $INSERT_INIT_DATA_SQL_PATH -U $DATABASE_USER $DATABASE_NAME