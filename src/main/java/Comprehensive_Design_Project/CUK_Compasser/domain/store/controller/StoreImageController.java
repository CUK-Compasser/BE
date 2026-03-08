package Comprehensive_Design_Project.CUK_Compasser.domain.store.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp.StoreImageListRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.service.StoreImageService;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreImageController {

    private final StoreImageService storeImageService;

    @GetMapping("/{storeId}/images")
    public StoreImageListRespDTO list(
            @PathVariable Long storeId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return storeImageService.getImages(storeId, memberId);
    }

    @PatchMapping("/{storeId}/images")
    public StoreImageListRespDTO upload(
            @PathVariable Long storeId,
            @RequestPart("storeImage") MultipartFile storeImage,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return storeImageService.uploadRepresentativeImage(storeId, memberId, storeImage);
    }

    @DeleteMapping("/{storeId}/images/{imageId}")
    public void delete(
            @PathVariable Long storeId,
            @PathVariable Long imageId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        storeImageService.deleteImage(storeId, imageId, memberId);
    }
}