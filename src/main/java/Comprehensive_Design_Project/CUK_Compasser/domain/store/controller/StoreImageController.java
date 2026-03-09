package Comprehensive_Design_Project.CUK_Compasser.domain.store.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreImageListRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.service.StoreImageService;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owners/me/store/images")
public class StoreImageController {

    private final StoreImageService storeImageService;

    @GetMapping
    public StoreImageListRespDTO list(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return storeImageService.getImages(memberId);
    }

    @PatchMapping
    public StoreImageListRespDTO upload(
            @RequestPart("storeImage") MultipartFile storeImage,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return storeImageService.uploadRepresentativeImage(memberId, storeImage);
    }

    @DeleteMapping("/{imageId}")
    public void delete(
            @PathVariable Long imageId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        storeImageService.deleteImage(imageId, memberId);
    }
}