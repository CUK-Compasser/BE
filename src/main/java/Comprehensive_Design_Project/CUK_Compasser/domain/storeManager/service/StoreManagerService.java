package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreManagerService {

    @Transactional
    public void writingReward (Long storeManagerId){

        // findByStoreManagerID + Eager Fetch "Store" -> get store_id

        // rewardRepository 조회

        // 있으면 Increase

        // 없으면 new & save

    }
}
