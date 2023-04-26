-- 프로필 테이블 제거
DROP TABLE profile;

-- 프로필 테이블
CREATE TABLE profile (
    id UUID DEFAULT gen_random_uuid(),
    user_id VARCHAR(100) UNIQUE NOT NULL,
    sequence_id SERIAL UNIQUE NOT NULL,
    email varchar(100) UNIQUE NOT NULL,
    name varchar(100) UNIQUE NOT NULL,
    created_at timestamp with time zone DEFAULT NULL,
    created_by varchar(100) DEFAULT NULL,
    updated_at timestamp with time zone DEFAULT NULL,
    updated_by varchar(100) DEFAULT NULL,
    PRIMARY KEY (id)
);
