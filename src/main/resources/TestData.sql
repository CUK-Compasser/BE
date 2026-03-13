insert into members
(created_at, updated_at, email, member_name, password, phone, provider, provider_id, role, status) VALUES
    (NOW(), now(), 'test@email.com', 'testname', 'password', '01012345678', 'NORMAL', NULL, 'STORE_MANAGER', 'ACTIVE');

insert into store_managers (created_at, member_id, updated_at, verified_at, business_license_number)
values (now(), 1, now(), now(), 'test-number');

INSERT INTO stores (
    latitude, longitude, created_at, updated_at,
    store_manager_id, store_name, store_details,
    business_hours, input_address, jibun_address, road_address,
    store_email, tag
)
VALUES
-- 1번 매장: 성수동 힙한 베이커리
(
    37.5445770, 127.0559740, NOW(), NOW(),
    1, '성수 어니언 팩토리', '직접 구운 유기농 빵과 스페셜티 커피가 있는 곳입니다.',
    '{"mon": "09:00-22:00", "tue": "09:00-22:00", "wed": "09:00-22:00", "thu": "09:00-22:00", "fri": "09:00-23:00", "sat": "10:00-23:00", "sun": "10:00-22:00"}',
    '성동구 성수동2가 277-7', '서울특별시 성동구 성수동2가 277-7', '서울특별시 성동구 아차산로9길 8',
    'onion_ss@example.com', 'BAKERY'
);

-- 2번 매장: 강남역 조용한 작업용 카페
(
    37.4979420, 127.0276210, NOW(), NOW(),
    2, '코드앤커피 강남점', '조용한 분위기에서 코딩과 공부를 즐길 수 있는 IT 테마 카페입니다.',
    '{"mon": "08:00-23:00", "tue": "08:00-23:00", "wed": "08:00-23:00", "thu": "08:00-23:00", "fri": "08:00-24:00", "sat": "09:00-24:00", "sun": "09:00-22:00"}',
    '강남구 역삼동 820-1', '서울특별시 강남구 역삼동 820-1', '서울특별시 강남구 테헤란로 1길 10',
    'code_coffee@example.com', 'CAFE'
),
-- 3번 매장: 연남동 작은 디저트 샵
(
    37.5613340, 126.9230550, NOW(), NOW(),
    3, '연남 달콤상점', '매일 아침 직접 구운 마카롱과 수제 티를 판매합니다.',
    '{"mon": "closed", "tue": "12:00-20:00", "wed": "12:00-20:00", "thu": "12:00-20:00", "fri": "12:00-21:00", "sat": "11:00-21:00", "sun": "11:00-20:00"}',
    '마포구 연남동 255-2', '서울특별시 마포구 연남동 255-2', '서울특별시 마포구 성미산로 190',
    'yeonnam_sweet@example.com', 'DESSERT'
);