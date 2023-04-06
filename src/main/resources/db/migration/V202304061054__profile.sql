-- 프로필 테이블
CREATE TABLE profile (
    id UUID DEFAULT gen_random_uuid(),
    email varchar(100) UNIQUE NOT NULL,
    name varchar(100) UNIQUE NOT NULL,
    created_at timestamp with time zone DEFAULT NULL,
    created_by timestamp with time zone DEFAULT NULL,
    updated_at timestamp with time zone DEFAULT NULL,
    updated_by timestamp with time zone DEFAULT NULL,
    PRIMARY KEY (id)
);
