#!/bin/bash

# The script creates a new user and a new database 'g4t1' in your local Postgres.
# To run this script you need to set the db password in the environment variable SWE_DB_KEY either in your IDE or in the terminal.

# Connect to PostgreSQL and execute the SQL commands
psql -U postgres -d postgres -c "CREATE USER g4t1 WITH PASSWORD '${SWE_DB_KEY}';"
psql -U postgres -d postgres -c "CREATE DATABASE tempera OWNER g4t1;"
