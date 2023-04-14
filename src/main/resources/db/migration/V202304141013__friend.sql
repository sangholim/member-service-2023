-- 친구 테이블
CREATE TABLE friend (
    id UUID DEFAULT gen_random_uuid(),
    profile_id varchar(100) NOT NULL,
    name varchar(100) NOT NULL,
    created_at timestamp with time zone DEFAULT NULL,
    created_by varchar(100) DEFAULT NULL,
    updated_at timestamp with time zone DEFAULT NULL,
    updated_by varchar(100) DEFAULT NULL,
    PRIMARY KEY (id)
);

-- INDEX
CREATE INDEX idx_friend_profile_id on friend (profile_id);
