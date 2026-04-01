package Comprehensive_Design_Project.CUK_Compasser.domain.store.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp.*;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.req.StoreReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Tag;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.service.StoreService;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.ApiResponse;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.SuccessStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    /**
     * 내 가게 수정
     * PATCH /owners/me/store
     */
    @PatchMapping("/owners/me/store")
    @Operation(summary = "내 가게 정보 수정 API", description = "로그인한 점장의 가게 정보를 수정하는 API 입니다.")
    public StoreRespDTO patchMyStore(
            @RequestBody StoreUpdateReqDTO req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return storeService.updateStore(memberId, req);
    }

    /**
     * 내 가게 조회
     * GET /owners/me/store
     */
    @GetMapping("/owners/me/store")
    @Operation(summary = "내 가게 조회 API", description = "로그인한 점장의 가게 정보를 조회하는 API 입니다.")
    public StoreRespDTO getMyStore(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return storeService.getMyStore(memberId);
    }

    /**
     * 내 가게 위치 입력/수정
     * PATCH /owners/me/store/location
     */
    @PatchMapping("/owners/me/store/location")
    @Operation(summary = "내 가게 위치 수정 API", description = "로그인한 점장의 가게 위치 정보를 수정하는 API 입니다.")
    public StoreRespDTO patchMyStoreLocation(
            @RequestBody StoreLocationUpdateReqDTO req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return storeService.updateLocation(memberId, req);
    }

    @GetMapping
    @Operation(summary = "가게 조회 메인 페이지 조회 API", description = "사용자가 로그인 이후 연결되는 메인 가게 조회 API로, createdAt 기준 페이지네이션으로 10개 씩 반환하는 API 입니다.")
    public ApiResponse<List<StoreRespPagingDTO.GetStoreReqDTO>> getStoreList (
            /*@AuthenticationPrincipal CustomUserDetails userDetails,*/
            @RequestParam BigDecimal userLat,
            @RequestParam BigDecimal userLon,
            @RequestParam(defaultValue = "0") Integer page) {
        return ApiResponse.onSuccess(SuccessStatus.OK, storeService.getStoreList(userLat, userLon, page));
    }

    @GetMapping("/address")
    @Operation(summary = "특정 주소 기준 가게 조회 API", description = "사용자가 입력한 특정 주소를 기준으로 하는 가게 조회 API로, createdAt 기준 페이지네이션으로 10개 씩 반환하는 API 입니다.")
    public ApiResponse<Object> getStoreListByAddress(
            /*@AuthenticationPrincipal CustomUserDetails userDetails,*/
            @RequestParam String address,
            @RequestParam(defaultValue = "0") Integer page){
        return ApiResponse.onSuccess(SuccessStatus.OK, storeService.getStoreListByAddress(address, page));
    }

    @GetMapping("/tag/{tag}")
    @Operation(summary = "태그 별 가게 조회 API", description = "사용자가 고른 태그를 기준으로 가게를 페이지네이션 조회를 하는 API 입니다. PathVariable로 /tag/CAFE 형식입니다")
    public ApiResponse<List<StoreRespPagingDTO.GetStoreReqDTO>> getStoreListByTag (
            /*@AuthenticationPrincipal CustomUserDetails userDetails,*/
            @RequestParam ("userLat") BigDecimal userLat,
            @RequestParam ("userLon")BigDecimal userLon,
            @PathVariable Tag tag,
            @RequestParam(defaultValue = "0") Integer page) {
        return ApiResponse.onSuccess(SuccessStatus.OK, storeService.getStoreListByTag(userLat, userLon, tag, page));
    }


    /* 대학교도 특정 주소이므로, 해당 API는 안 쓸 듯? */
//    @GetMapping("/university/{university}")
//    @Operation(summary = "대학교 반경 가게 조회 API", description = "사용자가 고른 대학교를 기준으로 반경의 가게를 조회하는 API 입니다.")
    public ApiResponse<List<StoreRespDTO>> getStoreListByUniversity (
            /*@AuthenticationPrincipal CustomUserDetails userDetails,*/
            @RequestBody StoreReqDTO.StoreReqWithCoordinateAndTagDTO dto,
            @RequestParam(defaultValue = "0") Integer page){
        return null;
    }

//    @GetMapping("/member") // 지도 클릭 시 사용자 반경 가게 조회 API
//    @Operation(summary = "사용자 반경 가게 조회 API", description = "사용자의 위치 기준 반경의 가게의 조회하는  API 입니다.")
    public ApiResponse<List<StoreRespDTO>> getStoreListByMemberRadius (
            @RequestBody MemberReqDTO.MemberCoordinatesDTO coordinates
            /*@AuthenticationPrincipal CustomUserDetails userDetails*/){
        return null;
    }

    @GetMapping("/{storeId}/simple")
    @Operation(summary = "가게 단순 조회 API", description = "사용자 원하는 가게 대한 단순 정보를 요청/반환하는 API 입니다. 화면 기준 핀 클릭해서 나오게 되는 바텀 시트 바용 API 입니다!")
    public ApiResponse<SimpleStoreInfoDTO> getStoreSimple (@PathVariable Long storeId){
        return ApiResponse.onSuccess(SuccessStatus.OK, storeService.getSimpleStoreInfo(storeId));
    }

    @GetMapping("/{storeId}")
    @Operation(summary = "가게 상세 조회 API", description = "사용자 원하는 가게 대한 상세 정보를 요청/반환하는 API 입니다. 바텀 시트 바에서 상점 보러가기를 통해 상세 정보를 받는 API 입니다!")
    public ApiResponse<StoreRespDTO> getStore (@PathVariable Long storeId){
        return ApiResponse.onSuccess(SuccessStatus.OK, storeService.getStoreInfo(storeId));
    }

//    @GetMapping("/keyword")
//    @Operation(summary = "가게 검색 API", description = "사용자가 입력한 키워드를 사용자 원하는 가게 대한 상세 정보를 요청/반환하는 API 입니다. 이건 제가 착각해서 가게 이름을 사용자가 '직접' 입력해서 검색하는 API로 만들어놔서 안쓸 것 같아요...")
    public ApiResponse<List<StoreRespPagingDTO.GetStoreReqDTO>> getStoreListByKeyword (
            @RequestParam BigDecimal userLat,
            @RequestParam BigDecimal userLon,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") Integer page
    ){
        // ElasticSearch 를 활용한 검색이 필요?
        return ApiResponse.onSuccess(SuccessStatus.OK, storeService.getStoreListByKeyword(userLat, userLon, keyword, page));
    }

}
/**
 * (선택) 내 가게 조회 - 운영시간 확인용
 * GET /stores/{storeId}
 {
 "businessHours": {
 "timezone": "Asia/Seoul",
 "weekly": {
 "MON": {
 "open": "09:00",
 "close": "21:00",
 "break-time": {
 "start": "15:00",
 "end": "16:00"
 },
 "closed": false
 },
 "TUE": {
 "open": "09:00",
 "close": "21:00",
 "break-time": {
 "start": "15:00",
 "end": "16:00"
 },
 "closed": false
 },
 "WED": {
 "open": "09:00",
 "close": "21:00",
 "break-time": {
 "start": "15:00",
 "end": "16:00"
 },
 "closed": false
 },
 "THU": {
 "open": "09:00",
 "close": "21:00",
 "break-time": {
 "start": "15:00",
 "end": "16:00"
 },
 "closed": false
 },
 "FRI": {
 "open": "09:00",
 "close": "21:00",
 "break-time": {
 "start": "15:00",
 "end": "16:00"
 },
 "closed": false
 },
 "SAT": {
 "open": "09:00",
 "close": "21:00",
 "break-time": null,
 "closed": false
 },
 "SUN": {
 "open": null,
 "close": null,
 "break-time": null,
 "closed": true
 }
 }
 }
 }
 */