package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreImageRespDTO {
    private Long imageId;
    private Long storeId;
    private String imageUrl;
    private LocalDateTime createdAt;
    private boolean isDefault;
}