INSERT INTO users (name, email)
SELECT * FROM (SELECT 'John Doe', 'john.doe@example.com') AS tmp
WHERE NOT EXISTS (SELECT email FROM users WHERE email = 'john.doe@example.com');

INSERT INTO users (name, email)
SELECT * FROM (SELECT 'Jane Smith', 'jane.smith@example.com') AS tmp
WHERE NOT EXISTS (SELECT email FROM users WHERE email = 'jane.smith@example.com');