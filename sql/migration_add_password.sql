-- Add password column if not exists and set sample hashes
ALTER TABLE users ADD COLUMN IF NOT EXISTS password VARCHAR(255) DEFAULT '';
UPDATE users SET password = SHA2('student123',256) WHERE email='alice@example.com';
UPDATE users SET password = SHA2('admin123',256) WHERE email='raj@example.edu';