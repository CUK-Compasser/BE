package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreImageRespDTO {
    private Long imageId;
    private String imageUrl;
    private LocalDateTime createdAt;
}