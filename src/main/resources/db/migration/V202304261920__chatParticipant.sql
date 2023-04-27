-- 테이블 제거
DROP TABLE chat_participant;

-- 채팅 참가자 테이블
CREATE TABLE chat_participant (
    id UUID DEFAULT gen_random_uuid(),
    chat_id varchar(100) NOT NULL,
    profile_sequence_id integer NOT NULL,
    room_name varchar(255),
    created_at timestamp with time zone DEFAULT NULL,
    created_by varchar(100) DEFAULT NULL,
    updated_at timestamp with time zone DEFAULT NULL,
    updated_by varchar(100) DEFAULT NULL,
    PRIMARY KEY (id)
);

-- INDEX
CREATE INDEX idx_chat_participant_chat_id on chat_participant (chat_id);
CREATE INDEX idx_chat_participant_profile_sequence_id on chat_participant (profile_sequence_id);