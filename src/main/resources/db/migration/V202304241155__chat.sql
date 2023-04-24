-- 채팅 테이블
CREATE TABLE chat (
    id UUID DEFAULT gen_random_uuid(),
    image varchar(255) NOT NULL,
    created_at timestamp with time zone DEFAULT NULL,
    created_by varchar(100) DEFAULT NULL,
    updated_at timestamp with time zone DEFAULT NULL,
    updated_by varchar(100) DEFAULT NULL,
    PRIMARY KEY (id)
);

