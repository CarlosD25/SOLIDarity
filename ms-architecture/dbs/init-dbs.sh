#!/bin/bash
set -e
export PGPASSWORD=$POSTGRES_PASSWORD;

# Function to create a database and run its init script
create_database_and_run_script() {
    local db_name=$1
    local script_name=$2
    echo "Creating database '$db_name'"
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
        CREATE DATABASE $db_name;
EOSQL
    echo "Running script '$script_name' on database '$db_name'"
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="$db_name" -f "/docker-entrypoint-initdb.d/$script_name"
}

# Create databases and run their respective scripts
create_database_and_run_script "users_db" "script-bd-user.sql"
create_database_and_run_script "campanias_db" "script-bd-campania.sql"
create_database_and_run_script "donaciones_db" "script-bd-donacion.sql"
create_database_and_run_script "notifications_db" "script-bd-notification.sql"
