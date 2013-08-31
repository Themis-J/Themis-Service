Themis-Service
==============

## DESCRIPTION
This module maintains the server-side services for Themis system.

## Install development environment locally
- Install PostgreSql 9.1 or above
- Goto "{baseDir}/db/scripts" and run "./local_db_creation.ksh" (if it's windows, pls run ".bat" one)
- Goto "{baseDir}/db/" and run "ant dropAll update -DenvType=dev"

## Start web service locally
- Run "mvn jetty:run -P dev" to start a web server with maven embeded jetty server. 
