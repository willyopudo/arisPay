-- ============================================================
-- Phase 1: Seed configuration data (migrated from YAML files)
-- application='gateway', profile='default', label='main'
-- ============================================================

-- ── Gateway – shared across all profiles ──────────────────────────────────────
INSERT INTO properties (application, profile, label, key, value, created_by)
VALUES
    ('gateway', 'default', 'main', 'server.port',                                    '8222',              'migration'),
    ('gateway', 'default', 'main', 'eureka.client.register-with-eureka',             'false',             'migration'),
    ('gateway', 'default', 'main', 'spring.application.name',                        'gateway',           'migration'),
    ('gateway', 'default', 'main', 'spring.cloud.gateway.discovery.locator.enabled', 'true',              'migration'),
    ('gateway', 'default', 'main', 'management.tracing.sampling.probability',        '1.0',               'migration'),
    -- Route: students
    ('gateway', 'default', 'main', 'spring.cloud.gateway.routes[0].id',              'students',          'migration'),
    ('gateway', 'default', 'main', 'spring.cloud.gateway.routes[0].uri',             'http://localhost:8090', 'migration'),
    ('gateway', 'default', 'main', 'spring.cloud.gateway.routes[0].predicates[0]',  'Path=/api/v1/students/**', 'migration'),
    -- Route: schools
    ('gateway', 'default', 'main', 'spring.cloud.gateway.routes[1].id',              'schools',           'migration'),
    ('gateway', 'default', 'main', 'spring.cloud.gateway.routes[1].uri',             'http://localhost:8070', 'migration'),
    ('gateway', 'default', 'main', 'spring.cloud.gateway.routes[1].predicates[0]',  'Path=/api/v1/schools/**', 'migration')
ON CONFLICT (application, profile, label, key) DO NOTHING;

-- ── Discovery service (Eureka) configuration ──────────────────────────────────
INSERT INTO properties (application, profile, label, key, value, created_by)
VALUES
    ('discovery', 'default', 'main', 'server.port',                              '8761', 'migration'),
    ('discovery', 'default', 'main', 'eureka.instance.hostname',                 'localhost', 'migration'),
    ('discovery', 'default', 'main', 'eureka.client.register-with-eureka',       'false', 'migration'),
    ('discovery', 'default', 'main', 'eureka.client.fetch-registry',             'false', 'migration')
ON CONFLICT (application, profile, label, key) DO NOTHING;

-- ── Global / application-level defaults (application = 'application') ─────────
-- These apply to ALL services that fetch from this config server
INSERT INTO properties (application, profile, label, key, value, created_by)
VALUES
    ('application', 'default', 'main', 'management.endpoints.web.exposure.include', '*',   'migration'),
    ('application', 'default', 'main', 'management.tracing.sampling.probability',   '1.0', 'migration')
ON CONFLICT (application, profile, label, key) DO NOTHING;

