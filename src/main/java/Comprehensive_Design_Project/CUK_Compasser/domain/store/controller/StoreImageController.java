package Comprehensive_Design_Project.CUK_Compasser.domain.store.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp.StoreImageRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.service.StoreImageService;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owners/me/store/image")
public class StoreImageController {

    private final StoreImageService storeImageService;

    @GetMapping
    @Operation(summary = "스토어 대표 사진 조회")
    public StoreImageRespDTO get(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return storeImageService.getRepresentativeImage(memberId);
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "스토어 대표 사진 교체")
    public StoreImageRespDTO upload(
            @Parameter(
                    description = "업로드할 이미지 파일",
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestParam("storeImage") MultipartFile storeImage,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return storeImageService.uploadRepresentativeImage(memberId, storeImage);
    }

    @DeleteMapping
    @Operation(summary = "스토어 대표 사진 삭제")
    public void delete(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        storeImageService.deleteRepresentativeImage(memberId);
    }
}