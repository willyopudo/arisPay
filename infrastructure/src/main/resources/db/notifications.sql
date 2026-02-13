CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    event_type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    subtitle VARCHAR(500),
    icon VARCHAR(50),
    color VARCHAR(30),
    is_seen BOOLEAN NOT NULL DEFAULT FALSE,
    metadata TEXT,
    event_timestamp TIMESTAMP NOT NULL,
    record_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    is_enabled SMALLINT NOT NULL DEFAULT 1,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    created_date TIMESTAMP NOT NULL DEFAULT NOW(),
    modified_by VARCHAR(100),
    modified_date TIMESTAMP
);

CREATE INDEX idx_notifications_company ON notifications(company_id);
CREATE INDEX idx_notifications_unseen ON notifications(company_id, is_seen);
