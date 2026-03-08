package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreImageListRespDTO {
    private Long storeId;
    private List<StoreImageRespDTO> images;
}