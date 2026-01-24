-- Create user_preferences table
CREATE TABLE IF NOT EXISTS user_preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    theme_customizations JSONB,
    notification_preferences JSONB,
    language VARCHAR(10),
    timezone VARCHAR(50),
    currency VARCHAR(10),
    date_format VARCHAR(20),
    time_format VARCHAR(10),
    two_factor_enabled BOOLEAN DEFAULT FALSE,
    email_notifications_enabled BOOLEAN DEFAULT TRUE,
    custom_settings JSONB,
    created_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    CONSTRAINT fk_user_preferences_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create index on user_id for faster lookups
CREATE INDEX IF NOT EXISTS idx_user_preferences_user_id ON user_preferences(user_id);

-- Create GIN indexes for JSONB columns for better query performance
CREATE INDEX IF NOT EXISTS idx_user_preferences_theme ON user_preferences USING GIN (theme_customizations);
CREATE INDEX IF NOT EXISTS idx_user_preferences_notifications ON user_preferences USING GIN (notification_preferences);
CREATE INDEX IF NOT EXISTS idx_user_preferences_custom_settings ON user_preferences USING GIN (custom_settings);

-- Add comment to table
COMMENT ON TABLE user_preferences IS 'Stores user-specific preferences and settings';
COMMENT ON COLUMN user_preferences.theme_customizations IS 'JSON object containing theme preferences like colors, dark mode, etc.';
COMMENT ON COLUMN user_preferences.notification_preferences IS 'JSON object containing notification preferences like email, push, sms settings';
COMMENT ON COLUMN user_preferences.custom_settings IS 'JSON object for extensible custom settings';
