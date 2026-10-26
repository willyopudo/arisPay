-- ============================================================
-- Phase 1: Config Server – Properties table (JDBC backend)
-- Spring Cloud Config JDBC backend requires columns:
--   application, profile, label, key, value
-- ============================================================

CREATE TABLE IF NOT EXISTS properties (
    id          BIGSERIAL PRIMARY KEY,
    application VARCHAR(255) NOT NULL,
    profile     VARCHAR(255) NOT NULL DEFAULT 'default',
    label       VARCHAR(255) NOT NULL DEFAULT 'main',
    key         VARCHAR(512) NOT NULL,
    value       TEXT,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(255),
    CONSTRAINT uq_properties UNIQUE (application, profile, label, key)
);

CREATE INDEX IF NOT EXISTS idx_properties_app_profile_label
    ON properties (application, profile, label);

-- ── Audit table – tracks every change to a property ────────────────────────────
CREATE TABLE IF NOT EXISTS properties_audit (
    audit_id    BIGSERIAL PRIMARY KEY,
    property_id BIGINT,
    application VARCHAR(255),
    profile     VARCHAR(255),
    label       VARCHAR(255),
    key         VARCHAR(512),
    old_value   TEXT,
    new_value   TEXT,
    changed_by  VARCHAR(255),
    changed_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    operation   VARCHAR(50)  -- INSERT | UPDATE | DELETE
);

-- ── Trigger: keep updated_at current on every row update ──────────────────────
CREATE OR REPLACE FUNCTION fn_update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_properties_updated_at
    BEFORE UPDATE ON properties
    FOR EACH ROW EXECUTE FUNCTION fn_update_timestamp();

-- ── Trigger: populate audit table on INSERT / UPDATE / DELETE ─────────────────
CREATE OR REPLACE FUNCTION fn_audit_properties()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO properties_audit(property_id, application, profile, label, key, old_value, new_value, changed_by, operation)
        VALUES (OLD.id, OLD.application, OLD.profile, OLD.label, OLD.key, OLD.value, NULL, current_user, 'DELETE');
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO properties_audit(property_id, application, profile, label, key, old_value, new_value, changed_by, operation)
        VALUES (NEW.id, NEW.application, NEW.profile, NEW.label, NEW.key, OLD.value, NEW.value, current_user, 'UPDATE');
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO properties_audit(property_id, application, profile, label, key, old_value, new_value, changed_by, operation)
        VALUES (NEW.id, NEW.application, NEW.profile, NEW.label, NEW.key, NULL, NEW.value, current_user, 'INSERT');
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_properties_audit
    AFTER INSERT OR UPDATE OR DELETE ON properties
    FOR EACH ROW EXECUTE FUNCTION fn_audit_properties();

