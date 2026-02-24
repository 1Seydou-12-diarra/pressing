-- ============================================================
--  Init PostgreSQL — Pressing + Keycloak
-- ============================================================

-- Base Keycloak (séparée de la base applicative)
-- Cette commande s'exécute connecté en tant que pressing_user sur pressing_db
SELECT 'CREATE DATABASE keycloak_db OWNER postgres'
WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = 'keycloak_db'
)\gexec

-- Extension uuid pour pressing_db
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";