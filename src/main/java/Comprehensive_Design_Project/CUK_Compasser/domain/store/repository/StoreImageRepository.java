package Comprehensive_Design_Project.CUK_Compasser.domain.store.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.StoreImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {

    boolean existsByStore_Id(Long storeId);

    List<StoreImage> findAllByStore_IdOrderByCreatedAtAsc(Long storeId);

    Optional<StoreImage> findByIdAndStore_Id(Long imageId, Long storeId);

    void deleteAllByStore_Id(Long storeId);
}