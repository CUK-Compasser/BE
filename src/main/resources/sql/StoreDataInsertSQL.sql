-- ---------------------------------------------------------
-- 3. stores 데이터 삽입 (store_manager_id와 1:1 매칭)
-- 일반 데이터 기준:   37.5445, 127.0560
-- 가톨릭대 정문:      37.4855, 126.8025
-- ---------------------------------------------------------
INSERT IGNORE INTO  stores (latitude, longitude, created_at, updated_at,
                    store_manager_id, store_name, store_details,
                    business_hours, input_address, jibun_address, road_address,
                    store_email, tag
) VALUES
-- 그룹 1: 성수 (매니저 1)
(37.5445770, 127.0559740, NOW(), NOW(),
 1, '성수 어니언 팩토리', '직접 구운 유기농 빵과 스페셜티 커피가 있는 곳입니다.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "TUE": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "WED": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "THU": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "FRI": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "SAT": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false },
       "SUN": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false }
     }
   }
 }',
 '성동구 성수동2가 277-7', '서울특별시 성동구 성수동2가 277-7', '서울특별시 성동구 아차산로9길 8',
 'onion_ss@test.com', 'BAKERY'
),
-- 그룹 2: 강남 (매니저 2)
(37.4979420, 127.0276210, NOW(), NOW(),
 2, '코드앤커피 강남점', '개발자를 위한 조용한 작업 공간과 진한 에스프레소.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "TUE": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "WED": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "THU": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "FRI": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "SAT": { "open": null, "close": null, "break-time": null, "closed": true },
       "SUN": { "open": null, "close": null, "break-time": null, "closed": true }
     }
   }
 }',
 '강남구 역삼동 820-1', '서울특별시 강남구 역삼동 820-1', '서울특별시 강남구 테헤란로 1길 10',
 'code_coffee@test.com', 'CAFE'
),
-- 그룹 3: 연남 (매니저 3)
(37.5613340, 126.9230550, NOW(), NOW(),
 3, '연남 달콤상점', '매일 아침 직접 구운 마카롱과 수제 티 전문점.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "TUE": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "WED": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "THU": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "FRI": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "SAT": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false },
       "SUN": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false }
     }
   }
 }',
 '마포구 연남동 255-2', '서울특별시 마포구 연남동 255-2', '서울특별시 마포구 성미산로 190',
 'yeonnam_sweet@test.com', 'BAKERY'
),
-- 그룹 4: 이태원 (매니저 4)
(37.5348330, 126.9926440, NOW(), NOW(),
 4, '글로벌 키친 이태원', '전 세계의 다양한 가정식을 한곳에서 맛볼 수 있는 레스토랑.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": null, "close": null, "break-time": null, "closed": true },
       "TUE": { "open": "17:00", "close": "01:00", "break-time": null, "closed": false },
       "WED": { "open": "17:00", "close": "01:00", "break-time": null, "closed": false },
       "THU": { "open": "17:00", "close": "01:00", "break-time": null, "closed": false },
       "FRI": { "open": "17:00", "close": "03:00", "break-time": null, "closed": false },
       "SAT": { "open": "17:00", "close": "03:00", "break-time": null, "closed": false },
       "SUN": { "open": "17:00", "close": "23:00", "break-time": null, "closed": false }
     }
   }
 }',
 '용산구 이태원동 123-5', '서울특별시 용산구 이태원동 123-5', '서울특별시 용산구 이태원로 150',
 'global_k@test.com', 'RESTAURANT'
),

-- 그룹 5: 판교 (매니저 5)
(37.4020050, 127.1089180, NOW(), NOW(),
 5, '판교 테크 베이커리', '바쁜 직장인들을 위한 영양 가득한 샌드위치와 베이커리.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": null, "close": null, "break-time": null, "closed": true },
       "TUE": { "open": null, "close": null, "break-time": null, "closed": true },
       "WED": { "open": "12:00", "close": "20:00", "break-time": null, "closed": false },
       "THU": { "open": "12:00", "close": "20:00", "break-time": null, "closed": false },
       "FRI": { "open": "12:00", "close": "21:00", "break-time": null, "closed": false },
       "SAT": { "open": "11:00", "close": "21:00", "break-time": null, "closed": false },
       "SUN": { "open": "11:00", "close": "19:00", "break-time": null, "closed": false }
     }
   }
 }',
 '분당구 삼평동 670', '경기도 성남시 분당구 삼평동 670', '경기도 성남시 분당구 판교역로 231',
 'pangyo_b@test.com', 'BAKERY'
),

(37.5407, 127.0700, NOW(), NOW(),
 6, '건대 맛도리 박스', '대학가 가성비 최고의 도시락 랜덤박스!',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": null, "close": null, "break-time": null, "closed": true },
       "TUE": { "open": "17:00", "close": "01:00", "break-time": null, "closed": false },
       "WED": { "open": "17:00", "close": "01:00", "break-time": null, "closed": false },
       "THU": { "open": "17:00", "close": "01:00", "break-time": null, "closed": false },
       "FRI": { "open": "17:00", "close": "03:00", "break-time": null, "closed": false },
       "SAT": { "open": "17:00", "close": "03:00", "break-time": null, "closed": false },
       "SUN": { "open": "17:00", "close": "23:00", "break-time": null, "closed": false }
     }
   }
 }',
 '광진구 화양동 7-3', '서울특별시 광진구 화양동 7-3', '서울특별시 광진구 능동로 120',
 'ku_box@test.com', 'RESTAURANT'),

-- 7. 왕십리 (조회 대상: 약 2.1km)
(37.5612, 127.0384, NOW(), NOW(),
 7, '왕십리 엔터박스', '엔터식스 쇼핑 후 즐기는 시원한 커피와 디저트 세트.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "TUE": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "WED": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "THU": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "FRI": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "SAT": { "open": null, "close": null, "break-time": null, "closed": true },
       "SUN": { "open": null, "close": null, "break-time": null, "closed": true }
     }
   }
 }',
 '성동구 행당동 155-1', '서울특별시 성동구 행당동 155-1', '서울특별시 성동구 왕십리광장로 17',
 'ws_enter@test.com', 'CAFE'),

-- 8. 잠실 (제외 대상: 약 4.2km)
(37.5133, 127.1001, NOW(), NOW(),
 8, '잠실 롯데 타워 박스', '화려한 롯데타워 뷰와 함께 즐기는 프리미엄 디저트.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": null, "close": null, "break-time": null, "closed": true },
       "TUE": { "open": null, "close": null, "break-time": null, "closed": true },
       "WED": { "open": "12:00", "close": "20:00", "break-time": null, "closed": false },
       "THU": { "open": "12:00", "close": "20:00", "break-time": null, "closed": false },
       "FRI": { "open": "12:00", "close": "21:00", "break-time": null, "closed": false },
       "SAT": { "open": "11:00", "close": "21:00", "break-time": null, "closed": false },
       "SUN": { "open": "11:00", "close": "19:00", "break-time": null, "closed": false }
     }
   }
 }',
 '송파구 신천동 29', '서울특별시 송파구 신천동 29', '서울특별시 송파구 올림픽로 300',
 'jamsil_l@test.com', 'BAKERY'),

-- 9. 서울숲 (조회 대상: 약 0.9km)
(37.5443, 127.0440, NOW(), NOW(),
 9, '서울숲 피크닉 세트', '서울숲 나들이 갈 때 딱 좋은 샌드위치 박스입니다.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": null, "close": null, "break-time": null, "closed": true },
       "TUE": { "open": null, "close": null, "break-time": null, "closed": true },
       "WED": { "open": "12:00", "close": "20:00", "break-time": null, "closed": false },
       "THU": { "open": "12:00", "close": "20:00", "break-time": null, "closed": false },
       "FRI": { "open": "12:00", "close": "21:00", "break-time": null, "closed": false },
       "SAT": { "open": "11:00", "close": "21:00", "break-time": null, "closed": false },
       "SUN": { "open": "11:00", "close": "19:00", "break-time": null, "closed": false }
     }
   }
 }',
 '성동구 성수동1가 685', '서울특별시 성동구 성수동1가 685', '서울특별시 성동구 뚝섬로 273',
 'forest_p@test.com', 'BAKERY'),

-- 10. 한양대 (조회 대상: 약 1.8km)
(37.5555, 127.0436, NOW(), NOW(),
 10, '한양대 공대생 간식', '밤샘 공부엔 역시 든든한 야식 랜덤박스.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "TUE": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "WED": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "THU": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "FRI": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "SAT": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false },
       "SUN": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false }
     }
   }
 }',
 '성동구 사근동 110', '서울특별시 성동구 사근동 110', '서울특별시 성동구 사근동길 2',
 'hanyang_s@test.com', 'BAKERY'),

-- 11. 압구정 (조회 대상: 약 2.7km)
(37.5270, 127.0284, NOW(), NOW(),
 11, '압구정 명품 랜덤박스', '압구정 로데오의 감성을 담은 세련된 요리 모음.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": null, "close": null, "break-time": null, "closed": true },
       "TUE": { "open": "17:00", "close": "01:00", "break-time": null, "closed": false },
       "WED": { "open": "17:00", "close": "01:00", "break-time": null, "closed": false },
       "THU": { "open": "17:00", "close": "01:00", "break-time": null, "closed": false },
       "FRI": { "open": "17:00", "close": "03:00", "break-time": null, "closed": false },
       "SAT": { "open": "17:00", "close": "03:00", "break-time": null, "closed": false },
       "SUN": { "open": "17:00", "close": "23:00", "break-time": null, "closed": false }
     }
   }
 }',
 '강남구 압구정동 369-1', '서울특별시 강남구 압구정동 369-1', '서울특별시 강남구 압구정로 201',
 'apgu_m@test.com', 'RESTAURANT'),

-- 12. 논현 (제외 대상: 약 3.8km)
(37.5111, 127.0215, NOW(), NOW(),
 12, '논현동 심야 상자', '야근하는 직장인을 위한 스트레스 해소 매운맛 박스.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "TUE": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "WED": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "THU": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "FRI": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "SAT": { "open": "11:00", "close": "15:00", "break-time": null, "closed": false },
       "SUN": { "open": null, "close": null, "break-time": null, "closed": true }
     }
   }
 }',
 '강남구 논현동 142-2', '서울특별시 강남구 논현동 142-2', '서울특별시 강남구 학동로 2길 15',
 'nonhyeon_s@test.com', 'RESTAURANT'),

-- 13. 청담 (조회 대상: 약 2.4km)
(37.5191, 127.0519, NOW(), NOW(),
 13, '청담 고급 디저트', '프랑스 파티시에가 직접 만든 고품격 구움과자.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "TUE": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "WED": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "THU": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "FRI": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "SAT": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false },
       "SUN": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false }
     }
   }
 }',
 '강남구 청담동 80-1', '서울특별시 강남구 청담동 80-1', '서울특별시 강남구 선릉로 152길 5',
 'cheongdam_d@test.com', 'BAKERY'),

-- 14. 천호 (제외 대상: 약 6.5km)
(37.5385, 127.1235, NOW(), NOW(),
 14, '천호 쭈꾸미 박스', '천호동 쭈꾸미 골목의 화끈한 맛을 집에서 즐기세요.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "TUE": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "WED": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "THU": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "FRI": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "SAT": { "open": "11:00", "close": "15:00", "break-time": null, "closed": false },
       "SUN": { "open": null, "close": null, "break-time": null, "closed": true }
     }
   }
 }',
 '강동구 천호동 411-5', '서울특별시 강동구 천호동 411-5', '서울특별시 강동구 천호대로 1005',
 'cheonho_j@test.com', 'RESTAURANT'),

-- 15. 군자 (조회 대상: 약 2.2km)
(37.5572, 127.0795, NOW(), NOW(),
 15, '군자역 가성비 박스', '퇴근길에 쓱! 가격 파괴 미친 구성 랜덤박스.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "TUE": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "WED": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "THU": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "FRI": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "SAT": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false },
       "SUN": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false }
     }
   }
 }',
 '광진구 군자동 48-11', '서울특별시 광진구 군자동 48-11', '서울특별시 광진구 군자로 101',
 'gunja_v@test.com', 'BAKERY'),

(37.5472, 127.0474, NOW(), NOW(),
 16, '뚝섬 블루 카페', '스페셜티 커피와 조용한 분위기의 카페입니다.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "TUE": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "WED": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "THU": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "FRI": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "SAT": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "SUN": { "open": "12:00", "close": "24:00", "break-time": null, "closed": false }
     }
   }
 }',
 '성동구 성수동1가 13-167', '서울특별시 성동구 성수동1가 13-167', '서울특별시 성동구 상원길 15',
 'ttuk_cafe@test.com', 'CAFE'),

-- 17. 자양 (약 1.5km, 조회됨)
(37.5340, 127.0680, NOW(), NOW(),
 17, '자양동 소금빵집', '갓 구운 소금빵이 맛있는 동네 베이커리.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "TUE": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "WED": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "THU": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "FRI": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "SAT": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false },
       "SUN": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false }
     }
   }
 }',
 '광진구 자양동 606', '서울특별시 광진구 자양동 606', '서울특별시 광진구 자양로 50',
 'jayang_b@test.com', 'BAKERY'),

-- 18. 구의 (약 2.5km, 조회됨)
(37.5460, 127.0850, NOW(), NOW(),
 18, '구의역 파스타 하우스', '정통 이탈리안 파스타를 가성비 있게 즐기세요.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "TUE": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "WED": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "THU": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "FRI": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "SAT": { "open": "11:00", "close": "15:00", "break-time": null, "closed": false },
       "SUN": { "open": null, "close": null, "break-time": null, "closed": true }
     }
   }
 }',
 '광진구 구의동 252', '서울특별시 광진구 구의동 252', '서울특별시 광진구 아차산로 380',
 'guui_rest@test.com', 'RESTAURANT'),

-- 19. 화양 (약 1.8km, 조회됨)
(37.5500, 127.0750, NOW(), NOW(),
 19, '화양동 디저트 빌리지', '다양한 조각 케이크가 가득한 디저트 카페.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "TUE": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "WED": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "THU": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "FRI": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "SAT": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "SUN": { "open": "12:00", "close": "24:00", "break-time": null, "closed": false }
     }
   }
 }',
 '광진구 화양동 111', '서울특별시 광진구 화양동 111', '서울특별시 광진구 군자로 15',
 'hwayang_c@test.com', 'CAFE'),

-- 20. 마장 (약 2.6km, 조회됨)
(37.5660, 127.0450, NOW(), NOW(),
 20, '마장 스테이크 하우스', '질 좋은 고기를 합리적인 가격에 제공합니다.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "TUE": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "WED": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "THU": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "FRI": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "SAT": { "open": "11:00", "close": "15:00", "break-time": null, "closed": false },
       "SUN": { "open": null, "close": null, "break-time": null, "closed": true }
     }
   }
 }',
 '성동구 마장동 510', '서울특별시 성동구 마장동 510', '서울특별시 성동구 살곶이길 18',
 'majang_r@test.com', 'RESTAURANT'),

-- 21. 장안 (약 2.0km, 조회됨)
(37.5610, 127.0650, NOW(), NOW(),
 21, '장안동 깜빠뉴', '천연 발효종을 사용하는 건강한 빵집.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "TUE": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "WED": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "THU": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "FRI": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "SAT": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false },
       "SUN": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false }
     }
   }
 }',
 '동대문구 장안동 370', '서울특별시 동대문구 장안동 370', '서울특별시 동대문구 장한로 10',
 'jangan_b@test.com', 'BAKERY'),

-- 22. 건대입구 (약 1.3km, 조회됨)
(37.5407, 127.0700, NOW(), NOW(),
 22, '건대 라운지 카페', '대학가 풍경이 한눈에 들어오는 뷰 맛집 카페.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "TUE": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "WED": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "THU": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "FRI": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "SAT": { "open": null, "close": null, "break-time": null, "closed": true },
       "SUN": { "open": null, "close": null, "break-time": null, "closed": true }
     }
   }
 }',
 '광진구 화양동 5', '서울특별시 광진구 화양동 5', '서울특별시 광진구 능동로 110',
 'konkuk_c@test.com', 'CAFE'),

-- 23. 삼성 (약 3.5km, 제외 대상)
(37.5150, 127.0400, NOW(), NOW(),
 23, '코엑스 브런치', '도심 속 여유를 즐기는 브런치 레스토랑.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "TUE": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "WED": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "THU": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "FRI": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "SAT": { "open": "11:00", "close": "15:00", "break-time": null, "closed": false },
       "SUN": { "open": null, "close": null, "break-time": null, "closed": true }
     }
   }
 }',
 '강남구 삼성동 159', '서울특별시 강남구 삼성동 159', '서울특별시 강남구 영동대로 513',
 'coex_r@test.com', 'RESTAURANT'),

-- 24. 회기 (약 4.0km, 제외 대상)
(37.5800, 127.0600, NOW(), NOW(),
 24, '경희대 앞 빵집', '추억이 깃든 대학가 오래된 베이커리.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "TUE": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "WED": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "THU": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "FRI": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "SAT": { "open": "11:00", "close": "15:00", "break-time": null, "closed": false },
       "SUN": { "open": null, "close": null, "break-time": null, "closed": true }
     }
   }
 }',
 '동대문구 회기동 1', '서울특별시 동대문구 회기동 1', '서울특별시 동대문구 경희대로 26',
 'hoegi_b@test.com', 'BAKERY'),

-- 25. 가락 (약 6.0km, 제외 대상)
(37.5000, 127.1000, NOW(), NOW(),
 25, '가락시장 신선 카페', '시장 근처 신선한 과일 주스가 유명한 카페.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "TUE": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "WED": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "THU": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "FRI": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "SAT": { "open": null, "close": null, "break-time": null, "closed": true },
       "SUN": { "open": null, "close": null, "break-time": null, "closed": true }
     }
   }
 }',
 '송파구 가락동 600', '서울특별시 송파구 가락동 600', '서울특별시 송파구 양재대로 932',
 'garak_c@test.com', 'CAFE'),

(37.4840, 126.8115, NOW(), NOW(),
 26, '역곡역 감성 카페', '역곡역 인근 카공족을 위한 조용한 명소입니다.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "TUE": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "WED": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "THU": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "FRI": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "SAT": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "SUN": { "open": "12:00", "close": "24:00", "break-time": null, "closed": false }
     }
   }
 }',
 '부천시 역곡동 74', '경기도 부천시 역곡동 74', '경기도 부천시 경인로 505',
 'yg_cafe@test.com', 'CAFE'
),
(37.4875, 126.7915, NOW(), NOW(),
 27, '소사 마스터 베이커리', '매일 아침 8시에 갓 구운 식빵이 나옵니다.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "TUE": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "WED": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "THU": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "FRI": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "SAT": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false },
       "SUN": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false }
     }
   }
 }',
 '부천시 소사본동 65', '경기도 부천시 소사본동 65', '경기도 부천시 소사로 170',
 'sosa_b@test.com', 'BAKERY'
),
(37.4915, 126.8235, NOW(), NOW(),
 28, '온수역 파스타 전문점', '온수역 근처 숨은 양식 맛집, 생면 파스타 전문.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "TUE": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "WED": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "THU": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "FRI": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "SAT": { "open": "11:00", "close": "15:00", "break-time": null, "closed": false },
       "SUN": { "open": null, "close": null, "break-time": null, "closed": true }
     }
   }
 }',
 '구로구 온수동 45', '서울특별시 구로구 온수동 45', '서울특별시 구로구 경인로 3길 10',
 'onsu_r@test.com', 'RESTAURANT'
),
(37.5035, 126.7975, NOW(), NOW(),
 29, '부천 종합운동장 카페', '공원 산책로와 연결된 탁 트인 뷰를 가진 카페.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "TUE": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "WED": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "THU": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "FRI": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "SAT": { "open": "12:00", "close": "02:00", "break-time": null, "closed": false },
       "SUN": { "open": "12:00", "close": "24:00", "break-time": null, "closed": false }
     }
   }
 }',
 '부천시 춘의동 8', '경기도 부천시 춘의동 8', '경기도 부천시 길주로 486',
 'stadium_c@test.com', 'CAFE'
),
(37.4955, 126.8315, NOW(), NOW(),
 30, '궁동 베이커리 하우스', '천연 발효종을 사용한 건강한 호밀빵 전문점.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "TUE": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "WED": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "THU": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "FRI": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "SAT": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false },
       "SUN": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false }
     }
   }
 }',
 '구로구 궁동 213', '서울특별시 구로구 궁동 213', '서울특별시 구로구 오리로 21길 5',
 'gung_b@test.com', 'BAKERY'
),

-- 3~4km 내외 (경계선)
(37.4725, 126.8125, NOW(), NOW(),
 31, '범박동 브런치 카페', '조용한 주택가에서 즐기는 여유로운 브런치.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "TUE": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "WED": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "THU": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "FRI": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "SAT": { "open": null, "close": null, "break-time": null, "closed": true },
       "SUN": { "open": null, "close": null, "break-time": null, "closed": true }
     }
   }
 }',
 '부천시 범박동 155', '경기도 부천시 범박동 155', '경기도 부천시 양지로 20',
 'beom_c@test.com', 'CAFE'
),
(37.4945, 126.8555, NOW(), NOW(),
 32, '개봉동 돈까스 팩토리', '한정 수량으로 판매하는 수제 돈까스 맛집.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "TUE": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "WED": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "THU": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "FRI": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "SAT": { "open": "11:00", "close": "15:00", "break-time": null, "closed": false },
       "SUN": { "open": null, "close": null, "break-time": null, "closed": true }
     }
   }
 }',
 '구로구 개봉동 170', '서울특별시 구로구 개봉동 170', '서울특별시 구로구 개봉로 15',
 'gb_r@test.com', 'RESTAURANT'
),
(37.4855, 126.8375, NOW(), NOW(),
 33, '천왕역 로컬 베이커리', '동네 주민들이 즐겨 찾는 아기자기한 빵집.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "TUE": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "WED": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "THU": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "FRI": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "SAT": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false },
       "SUN": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false }
     }
   }
 }',
 '구로구 천왕동 12', '서울특별시 구로구 천왕동 12', '서울특별시 구로구 천왕로 10',
 'cw_b@test.com', 'BAKERY'
),
(37.4925, 126.7655, NOW(), NOW(),
 34, '부천 중동 이탈리안', '기념일에 오기 좋은 고급스러운 이탈리안 레스토랑.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "TUE": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "WED": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "THU": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "FRI": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "SAT": { "open": "11:00", "close": "15:00", "break-time": null, "closed": false },
       "SUN": { "open": null, "close": null, "break-time": null, "closed": true }
     }
   }
 }',
 '부천시 중동 1141', '경기도 부천시 중동 1141', '경기도 부천시 길주로 180',
 'jd_r@test.com', 'RESTAURANT'
),
(37.4795, 126.8555, NOW(), NOW(),
 35, '광명사거리 카페', '광명사거리역 인근 로스터리 핸드드립 전문점.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "TUE": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "WED": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "THU": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "FRI": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "SAT": { "open": null, "close": null, "break-time": null, "closed": true },
       "SUN": { "open": null, "close": null, "break-time": null, "closed": true }
     }
   }
 }',
 '광명시 광명동 158', '경기도 광명시 광명동 158', '경기도 광명시 오리로 950',
 'gm_c@test.com', 'CAFE'
),

-- 5~6km 내외 (멀음)
(37.4945, 126.8455, NOW(), NOW(),
 36, '오류동 스테이크', '참나무 장작으로 구운 불맛 스테이크 하우스.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": null, "close": null, "break-time": null, "closed": true },
       "TUE": { "open": "17:00", "close": "01:00", "break-time": null, "closed": false },
       "WED": { "open": "17:00", "close": "01:00", "break-time": null, "closed": false },
       "THU": { "open": "17:00", "close": "01:00", "break-time": null, "closed": false },
       "FRI": { "open": "17:00", "close": "03:00", "break-time": null, "closed": false },
       "SAT": { "open": "17:00", "close": "03:00", "break-time": null, "closed": false },
       "SUN": { "open": "17:00", "close": "23:00", "break-time": null, "closed": false }
     }
   }
 }',
 '구로구 오류동 135', '서울특별시 구로구 오류동 135', '서울특별시 구로구 경인로 190',
 'oryu_r@test.com', 'RESTAURANT'
),
(37.5005, 126.8655, NOW(), NOW(),
 37, '고척 스카이 베이커리', '고척돔 경기 날 필수 방문 코스, 마카롱 전문.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "TUE": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "WED": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "THU": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "FRI": { "open": "06:30", "close": "18:00", "break-time": null, "closed": false },
       "SAT": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false },
       "SUN": { "open": "07:00", "close": "17:00", "break-time": null, "closed": false }
     }
   }
 }',
 '구로구 고척동 76', '서울특별시 구로구 고척동 76', '서울특별시 구로구 경인로 430',
 'gocheok_b@test.com', 'BAKERY'
),
(37.4765, 126.8675, NOW(), NOW(),
 38, '철산역 만남 카페', '대규모 좌석과 카공 테라스가 있는 대형 카페.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "TUE": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "WED": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "THU": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "FRI": { "open": "06:00", "close": "17:00", "break-time": null, "closed": false },
       "SAT": { "open": null, "close": null, "break-time": null, "closed": true },
       "SUN": { "open": null, "close": null, "break-time": null, "closed": true }
     }
   }
 }',
 '광명시 철산동 241', '경기도 광명시 철산동 241', '경기도 광명시 철산로 20',
 'cheol_c@test.com', 'CAFE'
),
(37.4685, 126.8285, NOW(), NOW(),
 39, '옥길동 파스타', '옥길 지구 맘카페 소문난 패밀리 레스토랑.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "TUE": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "WED": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "THU": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "FRI": { "open": "11:00", "close": "20:30", "break-time": { "start": "14:30", "end": "17:30" }, "closed": false },
       "SAT": { "open": "11:00", "close": "15:00", "break-time": null, "closed": false },
       "SUN": { "open": null, "close": null, "break-time": null, "closed": true }
     }
   }
 }',
 '부천시 옥길동 710', '경기도 부천시 옥길동 710', '경기도 부천시 옥길로 110',
 'ok_r@test.com', 'RESTAURANT'
),
(37.5035, 126.7635, NOW(), NOW(),
 40, '부천시청 맛집 상자', '부천시청 직장인들이 애용하는 덮밥 도시락.',
 '{
   "businessHours": {
     "timezone": "Asia/Seoul",
     "weekly": {
       "MON": { "open": null, "close": null, "break-time": null, "closed": true },
       "TUE": { "open": "17:00", "close": "01:00", "break-time": null, "closed": false },
       "WED": { "open": "17:00", "close": "01:00", "break-time": null, "closed": false },
       "THU": { "open": "17:00", "close": "01:00", "break-time": null, "closed": false },
       "FRI": { "open": "17:00", "close": "03:00", "break-time": null, "closed": false },
       "SAT": { "open": "17:00", "close": "03:00", "break-time": null, "closed": false },
       "SUN": { "open": "17:00", "close": "23:00", "break-time": null, "closed": false }
     }
   }
 }',
 '부천시 중동 1156', '경기도 부천시 중동 1156', '경기도 부천시 소향로 181',
 'bc_r@test.com', 'RESTAURANT'
);