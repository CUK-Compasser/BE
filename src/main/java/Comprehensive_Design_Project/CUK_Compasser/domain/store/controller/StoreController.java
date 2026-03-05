package Comprehensive_Design_Project.CUK_Compasser.domain.store.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreLocationUpdateReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreUpdateReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.service.StoreService;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.ApiResponse;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    /**
     * 점장 가게 수정 (운영시간 raw JSON 포함)
     * PATCH /stores/{storeId}
     */
    @PatchMapping("/{storeId}")
    public StoreRespDTO patchStore(
            @PathVariable Long storeId,
            @RequestBody StoreUpdateReqDTO req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return storeService.updateStore(storeId, memberId, req);
    }

    @GetMapping("/{storeId}")
    public StoreRespDTO getMyStore(
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return storeService.getMyStore(storeId, memberId);
    }

    /**
     * 위치 입력/수정 (명세: PATCH /stores/location 이지만 storeId가 필요하니 보통 /{storeId}/location이 깔끔)
     * 너희 명세를 따르려면 storeId를 body로 받게 바꾸면 됨.
     */
    @PatchMapping("/{storeId}/location")
    public StoreRespDTO patchLocation(
            @PathVariable Long storeId,
            @RequestBody StoreLocationUpdateReqDTO req,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return storeService.updateLocation(storeId, memberId, req);
    }

    @GetMapping // 로그인 이후 바로 연결되는 메인 페이지, createdAt 기준 페이지네이션 10개
    @Operation(summary = "가게 조회 메인 페이지 조회 API", description = "사용자가 로그인 이후 연결되는 메인 가게 조회 API로, createdAt 기준 페이지네이션으로 10개 씩 반환하는 API 입니다.")
    public ApiResponse<List<StoreRespDTO>> getStoreList (
            @AuthenticationPrincipal CustomUserDetails userDetails){
        return null;
    }

    @GetMapping("/{tag}") // 태그 별 리스트  조회
    @Operation(summary = "태그 별 가게 조회 API", description = "사용자가 고른 태그를 기준으로 가게를 페이지네이션 조회를 하는 API 입니다.")
    public ApiResponse<List<StoreRespDTO>> getStoreListByTag (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String tag
    ){
        return null;
    }

    @GetMapping("/university/{university}") // 대학교 반경 기준 가게 조회 API
    @Operation(summary = "대학교 반경 가게 조회 API", description = "사용자가 고른 대학교를 기준으로 반경의 가게를 조회하는 API 입니다.")
    public ApiResponse<List<StoreRespDTO>> getStoreListByUniversity (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String university){
        return null;
    }

    @GetMapping("/member") // 지도 클릭 시 사용자 반경 가게 조회 API
    @Operation(summary = "사용자 반경 가게 조회 API", description = "사용자의 위치 기준 반경의 가게의 조회하는  API 입니다.")
    public ApiResponse<List<StoreRespDTO>> getStoreListByMemberRadius (
            @RequestBody MemberReqDTO.MemberCoordinatesDTO coordinates,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        return null;
    }
}
/**
 * (선택) 내 가게 조회 - 운영시간 확인용
 * GET /stores/{storeId}
 * {
 *   "businessHours": {
 *     "timezone": "Asia/Seoul",
 *     "weekly": {
 *       "MON": { "open": "09:00", "close": "21:00", "break-time": {"start": "15:00","end": "16:00"}, "closed": false}
 *       "TUE": { "open": "09:00", "close": "21:00", "break-time": {"start": "15:00","end": "16:00"}, "closed": false },
 *       "WED": { "open": "09:00", "close": "21:00", "break-time": {"start": "15:00","end": "16:00"}, "closed": false },
 *       "THU": { "open": "09:00", "close": "21:00", "break-time": {"start": "15:00","end": "16:00"}, "closed": false },
 *       "FRI": { "open": "09:00", "close": "21:00", "break-time": {"start": "15:00","end": "16:00"}, "closed": false },
 *       "SAT": { "open": "09:00", "close": "21:00", "break-time": null, "closed": false }, //브레이크 타임 없음
 *       "SUN": { "open": null, "close": null, "break-time": null, "closed": true }
 *     }
 *   }
 * }
 */