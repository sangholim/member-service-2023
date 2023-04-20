-- 인덱스 제거
DROP INDEX idx_friend_profile_id;

-- 친구 테이블 제거
DROP TABLE friend;

-- 친구 테이블
CREATE TABLE friend (
    id UUID DEFAULT gen_random_uuid(),
    subject_profile_id varchar(100) NOT NULL,
    object_profile_id varchar(100) NOT NULL,
    name varchar(100) NOT NULL,
    created_at timestamp with time zone DEFAULT NULL,
    created_by varchar(100) DEFAULT NULL,
    updated_at timestamp with time zone DEFAULT NULL,
    updated_by varchar(100) DEFAULT NULL,
    PRIMARY KEY (id)
);

-- 인덱스 추가
CREATE INDEX idx_friend_subject_profile_id on friend USING HASH(subject_profile_id);
CREATE INDEX idx_friend_object_profile_id on friend USING HASH(object_profile_id);
