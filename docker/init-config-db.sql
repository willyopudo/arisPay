-- Creates the config_db database used by the Config Server JDBC backend.
-- This script runs automatically when the PostgreSQL container first starts.

SELECT 'CREATE DATABASE config_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'config_db')\gexec

-- Grant the alibou user full access
GRANT ALL PRIVILEGES ON DATABASE config_db TO alibou;

