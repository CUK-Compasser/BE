package Comprehensive_Design_Project.CUK_Compasser.domain.store.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Tag;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByIdAndStoreManager_MemberId(Long storeId, Long memberId);

    Optional<Store> findByStoreManager_MemberId(Long memberId);

    /*
    * 반경 함수 기준 MySQL = ST_Distance_Sphere = 특정 DB 종속 함수라 NativeSQL이 필요
    */

    @Query("Select s From Store s where st_distance_sphere(Point(:userLat, :userLon), Point(s.latitude, s.longitude)) <= :distance")
    List<Store> findStoresWithinRadius(
            @Param("userLat") BigDecimal userLat,
            @Param("userLon") BigDecimal userLon,
            @Param("distance") double distanceInMeters);

    Page<Store> findAllByOrderByCreatedAtDesc(Pageable pageable); // 기본 조회 + 페이징

    @Query(value = "" +
            "Select * From store s " +
            "Where ST_Distance_Sphere(Point(:userLon, :userLat), Point(s.longitude, s.latitude)) <= :distance " +
            "ORDER BY s.created_at DESC",
            countQuery = "Select count(*) From store s " +
                    "Where ST_Distance_Sphere(Point(:userLon, :userLat), Point(s.longitude, s.latitude)) <= :distance",
            nativeQuery = true)
    Page<Store> findStoresWithinRadius(
            @Param("userLat") BigDecimal userLat,
            @Param("userLon") BigDecimal userLon,
            @Param("distance") double distanceInMeters,
            Pageable pageable); // 기본 조회 + 반경 + 페이징

    Page<Store> findAllByTagOrderByCreatedAtDesc(Tag tag, Pageable pageable); // 태그 별 조회 + 페이징

    @Query(value = "Select * From store s " +
            "Where ST_Distance_Sphere(Point(:userLon, :userLat), Point(s.longitude, s.latitude)) <= :distance " +
            "And s.tag = :tag " +
            "Order By s.created_at DESC",
            countQuery = "Select count(*) From store s " +
                    "Where ST_Distance_Sphere(Point(:userLon, :userLat), Point(s.longitude, s.latitude)) <= :distance " +
                    "And s.tag = :tag",
            nativeQuery = true)
    Page<Store> findStoresByTagWithinRadius(
            @Param("userLat") BigDecimal userLat,
            @Param("userLon") BigDecimal userLon,
            @Param("distance") double distanceInMeters,
            @Param("tag") String tag,
            Pageable pageable); // 태그 별 조회 + 반경 + 페이징

}