package Comprehensive_Design_Project.CUK_Compasser.domain.store.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByIdAndStoreManagerId(Long storeId, Long storeManagerId);

    Optional<Store> findByStoreManagerId(Long storeManagerId);

    boolean existsByStoreManagerId(Long storeManagerId);
}