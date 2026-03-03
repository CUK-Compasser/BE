package Comprehensive_Design_Project.CUK_Compasser.domain.store.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByStoreManager_MemberId(Long memberId);

    Optional<Store> findByIdAndStoreManager_MemberId(Long storeId, Long memberId);
}