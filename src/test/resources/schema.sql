-- 1. `member` 테이블: 회원 정보를 저장
CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 회원 ID (기본 키)
    name VARCHAR(255) NOT NULL,           -- 회원 이름
    role ENUM('LECTURER', 'ATTENDEE') NOT NULL -- 회원 역할 (LECTURER 또는 ATTENDEE)
);

-- 2. `lecture` 테이블: 강의 정보를 저장
CREATE TABLE lecture (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,      -- 강의 ID (기본 키)
    max_capacity INT NOT NULL,                 -- 최대 정원
    current_capacity INT NOT NULL,             -- 현재 정원
    title VARCHAR(255) NOT NULL,               -- 강의 제목
    lecturer_id BIGINT NOT NULL,               -- 강의자 ID (member 테이블 참조)
    start_time DATETIME NOT NULL,              -- 강의 시작 시간
    end_time DATETIME NOT NULL,                -- 강의 종료 시간
    recruitment_start_time DATETIME NOT NULL,  -- 모집 시작 시간
    recruitment_end_time DATETIME NOT NULL,    -- 모집 종료 시간
);

-- 3. `registration` 테이블: 등록 정보를 저장
CREATE TABLE registration (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,      -- 등록 ID (기본 키)
    attendee_id BIGINT NOT NULL,               -- 참가자 ID (member 테이블 참조)
    lecture_id BIGINT NOT NULL,                -- 강의 ID (lecture 테이블 참조)
    status ENUM('COMPLETED', 'CANCELED') NOT NULL, -- 등록 상태 (COMPLETED 또는 CANCELED)
    created_at DATETIME NOT NULL,              -- 등록 생성 시간
);
