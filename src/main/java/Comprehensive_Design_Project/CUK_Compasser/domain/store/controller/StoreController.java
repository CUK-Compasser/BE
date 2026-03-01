package Comprehensive_Design_Project.CUK_Compasser.domain.store.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreResponse;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreUpdateRequest;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.service.StoreService;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.ApiResponse;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public StoreResponse updateStore(
            @PathVariable Long storeId,
            @RequestBody StoreUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return storeService.updateStore(storeId, memberId, request);
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
    @GetMapping("/{storeId}")
    public StoreResponse getMyStore(
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return storeService.getMyStore(storeId, memberId);
    }


    @GetMapping // 로그인 이후 바로 연결되는 메인 페이지, createdAt 기준 페이지네이션 10개
    @Operation()
    public ApiResponse<Object> getStoreList (
            @AuthenticationPrincipal CustomUserDetails userDetails){
        return null;
    }

    @GetMapping("/{tag}") // 태그 별 리스트  조회
    @Operation()
    public ApiResponse<Object> getStoreListByTag (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String tag
            ){
        return null;
    }

    @GetMapping("/university/{university}") // 대학교 반경 기준 가게 조회 API
    @Operation()
    public ApiResponse<Object> getStoreListByUniversity (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String university){
        return null;
    }

    @GetMapping("/member") // 지도 클릭 시 사용자 반경 가게 조회 API
    @Operation()
    public ApiResponse<Object> getStoreListByMemberRadius (
            @RequestBody MemberReqDTO.MemberCoordinatesDTO coordinates,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        return null;
    }


}