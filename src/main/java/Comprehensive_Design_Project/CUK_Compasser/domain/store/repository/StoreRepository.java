package Comprehensive_Design_Project.CUK_Compasser.domain.store.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Tag;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByStoreManager_MemberId(Long memberId);

    Optional<Store> findByStoreManager_Id(Long storeManagerId);

    /*
    * 반경 함수 기준 MySQL = ST_Distance_Sphere = 특정 DB 종속 함수라 NativeSQL이 필요
    */

    /*@Query("Select s From Store s where st_distance_sphere(Point(:userLat, :userLon), Point(s.latitude, s.longitude)) <= :distance")
    List<Store> findStoresWithinRadius(
            @Param("userLat") BigDecimal userLat,
            @Param("userLon") BigDecimal userLon,
            @Param("distance") double distanceInMeters);*/

    Page<Store> findAllByOrderByCreatedAtDesc(Pageable pageable); // 기본 조회 + 페이징

    @Query(value = "Select * From stores s " +
            "Where ST_Distance_Sphere(Point(:userLon, :userLat), Point(s.longitude, s.latitude)) <= :distance " +
            "ORDER BY s.created_at DESC",
            countQuery = "Select count(*) From stores s " +
                    "Where ST_Distance_Sphere(Point(:userLon, :userLat), Point(s.longitude, s.latitude)) <= :distance",
            nativeQuery = true)
    Page<Store> findStoresWithinRadius(
            @Param("userLon") BigDecimal userLon,
            @Param("userLat") BigDecimal userLat,
            @Param("distance") double distance,
            Pageable pageable); // 기본 조회 + 반경 + 페이징

    Page<Store> findAllByTagOrderByCreatedAtDesc(Tag tag, Pageable pageable); // 태그 별 조회 + 페이징

    @Query("select s from Store s join fetch s.randomBoxes where s.id = :storeId")
    Optional<Store> findByIdJoinFetchRandomBox(@Param ("storeId")Long storeId);

    @Query(value = "Select * From stores" +
            " s " +
            "Where ST_Distance_Sphere(Point(:userLon, :userLat), Point(s.longitude, s.latitude)) <= :distance " +
            "And s.tag = :tag " +
            "Order By s.created_at DESC",
            countQuery = "Select count(*) From stores s " +
                    "Where ST_Distance_Sphere(Point(:userLon, :userLat), Point(s.longitude, s.latitude)) <= :distance " +
                    "And s.tag = :tag",
            nativeQuery = true)
    Page<Store> findStoresByTagWithinRadius(
            @Param("userLon") BigDecimal userLon,
            @Param("userLat") BigDecimal userLat,
            @Param("distance") double distance,
            @Param("tag") String tag,
            Pageable pageable); // 태그 별 조회 + 반경 + 페이징

    @Query(value = "Select * From stores s " +
            "Where ST_Distance_Sphere(Point(:userLon, :userLat), Point(s.longitude, s.latitude)) <= :distance " +
            "And s.store_name like :keyword " +
            "Order By s.created_at DESC",
            countQuery = "Select count(*) From stores s " +
                    "Where ST_Distance_Sphere(Point(:userLon, :userLat), Point(s.longitude, s.latitude)) <= :distance " +
                    "And s.store_name like :keyword",
            nativeQuery = true)
    Page<Store> findStoresByKeywordWithinRadius(
            @Param("userLon") BigDecimal userLon,
            @Param("userLat") BigDecimal userLat,
            @Param("distance") double distance,
            @Param("keyword") String keyword,
            Pageable pageable); // 가게 이름 조회 + 반경 + 페이징

}