package Comprehensive_Design_Project.CUK_Compasser.domain.store.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.StoreImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {

    Optional<StoreImage> findFirstByStore_IdOrderByCreatedAtAsc(Long storeId);

    void deleteAllByStore_Id(Long storeId);
}