package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.entity.StoreManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreManagerRepository extends JpaRepository<StoreManager, Long> {

    // 로그인한 memberId로 점장 정보 조회
    Optional<StoreManager> findByMemberId(Long memberId);

    // (선택) 점장 여부만 빠르게 체크
    boolean existsByMember_Id(Long memberId);
}