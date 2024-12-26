DELETE FROM member;
DELETE FROM lecture;
DELETE FROM registration;

INSERT INTO member (id, name, role) VALUES
    (1, '율무', 'LECTURER'),
    (2, '이다은', 'ATTENDEE'),
    (3, '황지수', 'ATTENDEE'),
    (4, '이호민', 'ATTENDEE'),
    (5, '이충헌', 'ATTENDEE'),
    (6, '윤희상', 'ATTENDEE'),
    (7, '김민수', 'ATTENDEE'),
    (8, '박지연', 'ATTENDEE'),
    (9, '최현우', 'ATTENDEE'),
    (10, '이승현', 'ATTENDEE'),
    (11, '정유진', 'ATTENDEE'),
    (12, '김하늘', 'ATTENDEE'),
    (13, '이소정', 'ATTENDEE'),
    (14, '유재석', 'ATTENDEE'),
    (15, '김연아', 'ATTENDEE'),
    (16, '강호동', 'ATTENDEE'),
    (17, '송지효', 'ATTENDEE'),
    (18, '이준호', 'ATTENDEE'),
    (19, '박찬호', 'ATTENDEE'),
    (20, '김나영', 'ATTENDEE'),
    (21, '이정재', 'ATTENDEE'),
    (22, '한지민', 'ATTENDEE'),
    (23, '장동건', 'ATTENDEE'),
    (24, '고소영', 'ATTENDEE'),
    (25, '한소희', 'ATTENDEE'),
    (26, '박보검', 'ATTENDEE'),
    (27, '아이유', 'ATTENDEE'),
    (28, '이효리', 'ATTENDEE'),
    (29, '장윤정', 'ATTENDEE'),
    (30, '김종국', 'ATTENDEE'),
    (31, '정형돈', 'ATTENDEE'),
    (32, '김다은', 'ATTENDEE'),
    (33, '오다은', 'ATTENDEE'),
    (34, '박다은', 'ATTENDEE'),
    (35, '유다은', 'ATTENDEE'),
    (36, '황다은', 'ATTENDEE'),
    (37, '민다은', 'ATTENDEE'),
    (38, '고다은', 'ATTENDEE'),
    (39, '장다은', 'ATTENDEE'),
    (40, '송다은', 'ATTENDEE'),
    (41, '한다은', 'ATTENDEE'),
    (42, '정다은', 'ATTENDEE');


INSERT INTO lecture (
    id, max_capacity, current_capacity,
    title, lecturer_id,
    start_time, end_time,
    recruitment_start_time, recruitment_end_time
) VALUES
    (1, 30, 0,
     'TDD & 클린아키텍처', 1,
     '2090-12-31 09:00:00', '2090-12-31 11:00:00',
     '2024-12-25 00:00:00', '2090-12-25 23:59:59');