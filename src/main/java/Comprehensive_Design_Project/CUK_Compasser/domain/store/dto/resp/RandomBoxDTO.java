package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp;

public record RandomBoxDTO (
        Long randomBoxId,
        String boxName,
        Integer price,
        Integer stock
) {
}
