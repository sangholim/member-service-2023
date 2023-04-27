-- 채팅 테이블 삭제
DROP TABLE chat;

-- 채팅 테이블
CREATE TABLE chat (
    id UUID DEFAULT gen_random_uuid(),
    image varchar(255) NOT NULL,
    combined_participant_profile_sequence_id TEXT UNIQUE NOT NULL,
    participant_count integer NOT NULL,
    created_at timestamp with time zone DEFAULT NULL,
    created_by varchar(100) DEFAULT NULL,
    updated_at timestamp with time zone DEFAULT NULL,
    updated_by varchar(100) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX idx_chat_combined_participant_profile_sequence_id on chat (combined_participant_profile_sequence_id);