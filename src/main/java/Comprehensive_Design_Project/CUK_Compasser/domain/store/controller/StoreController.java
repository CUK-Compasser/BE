package Comprehensive_Design_Project.CUK_Compasser.domain.store.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreLocationUpdateReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreUpdateReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.service.StoreService;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

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