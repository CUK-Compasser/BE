package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.entity.StoreManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreManagerRepository extends JpaRepository<StoreManager, Long> {

    // memberId로 점장 조회 (연관관계 기준)
    Optional<StoreManager> findByMember_Id(Long memberId);

    // 점장 여부 체크
    boolean existsByMember_Id(Long memberId);
}