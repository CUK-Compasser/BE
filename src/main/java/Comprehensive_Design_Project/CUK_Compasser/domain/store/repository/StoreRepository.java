package Comprehensive_Design_Project.CUK_Compasser.domain.store.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByIdAndStoreManager_MemberId(Long storeId, Long memberId);

    Optional<Store> findByStoreManager_MemberId(Long memberId);


    @Query("Select s From Store s where st_distance_sphere(Point(:userLat, :userLon), Point(s.latitude, s.longitude)) <= :distance")
    List<Store> findStoresWithinRadius(
            @Param("userLat") BigDecimal userLat,
            @Param("userLon") BigDecimal userLon,
            @Param("distance") double distanceInMeters);
}