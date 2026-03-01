package Comprehensive_Design_Project.CUK_Compasser.domain.store.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.StoreImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {

    boolean existsByStore_Id(Long storeId);
}