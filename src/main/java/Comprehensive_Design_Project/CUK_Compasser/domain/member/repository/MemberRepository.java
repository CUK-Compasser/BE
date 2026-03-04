package Comprehensive_Design_Project.CUK_Compasser.domain.member.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
