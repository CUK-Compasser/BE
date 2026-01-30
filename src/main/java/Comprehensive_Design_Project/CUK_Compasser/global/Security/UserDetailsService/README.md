## Authorization Flow


1. 점장님 or 사장님이 로그인을 요청한다.


2. 필터에서 등록된 CustomUserDetailsService 호출됨
    - 테이블이 나뉘어져 있는 상황
    - 모두 조회?
      - memberRepository.findByEmail()
      - storeManagerRepository.findByEmail()?
    - CustomUserDetails는 어떻게 담아야 하는가?
      - Interface로 공통 정보 구현 강제화? (member im)
      - ```java
        @Entity
        Member implements CommonInfo { }
        
        @Entity
        StoreManager implements CommonInfo { }
    
        // 이렇게 구조화 하였을 때 문제점?
        ```

3. 이후 플로우는 같음..
   - 존재하는 경우 해당 정보 기반 JWT 발급

---
## 해결책

1. Account 엔티티 추가
   - Member, StoreManager와 공통되는 정보를 포함 (Email, Password, Login, ...)
   - 기존 사용자과는 @OneToOne 관계 설정

2. In CustomUserDetails
   - 주입되는 대상을 Account 로 변경
   - OAuth2UserDetails -> 인터페이스화