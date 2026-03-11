package Comprehensive_Design_Project.CUK_Compasser;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Login;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.MemberRole;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Role;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberSaveTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("로그인 테스트를 위한 임시 계정 생성")
    @Rollback(false)
    void createTestMember() {

        // Given
        String email = "test@cuk.ac.kr";
        String rawPassword = "password123";

        // When
        Member member = Member.builder()
                .email(email)
                .memberName("테스트유저")
                .password(passwordEncoder.encode(rawPassword))
                .phone("01012341234")
                .provider(Login.NORMAL)
                .providerId(email)
                .role(MemberRole.NORMAL)
                .status(Role.ACTIVE)
                .build();

        Member savedMember = memberRepository.save(member);

        // Then
        assertThat(savedMember.getEmail()).isEqualTo(email);
        assertThat(passwordEncoder.matches(rawPassword, savedMember.getPassword())).isTrue();

        System.out.println("======================================");
        System.out.println("임시 유저 생성 완료");
        System.out.println("Email: " + savedMember.getEmail());
        System.out.println("Encoded Password: " + savedMember.getPassword());
        System.out.println("======================================");
    }
}