#!/bin/bash
# ============================================================
#  Init PostgreSQL — Pressing + Keycloak
# ============================================================

set -e

echo ">>> Création de la base keycloak_db si elle n'existe pas..."
psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" <<-EOSQL
    SELECT 'CREATE DATABASE keycloak_db OWNER postgres'
    WHERE NOT EXISTS (
        SELECT FROM pg_database WHERE datname = 'keycloak_db'
    )\gexec
EOSQL

echo ">>> Activation de l'extension uuid-ossp sur pressing_db..."
psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" <<-EOSQL
    CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
EOSQL

echo ">>> Initialisation terminée avec succès."