package Comprehensive_Design_Project.CUK_Compasser.domain.member.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Login;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByProviderAndProviderId(Login provider, String providerId);
}
