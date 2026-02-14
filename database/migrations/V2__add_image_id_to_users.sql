-- Add image_id column to users table to link profile pictures
ALTER TABLE users ADD COLUMN IF NOT EXISTS image_id BIGINT;

-- Add foreign key constraint to media table
ALTER TABLE users ADD CONSTRAINT fk_users_media
    FOREIGN KEY (image_id) REFERENCES media(id) ON DELETE SET NULL;
