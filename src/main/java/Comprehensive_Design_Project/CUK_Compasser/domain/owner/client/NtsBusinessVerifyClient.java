package Comprehensive_Design_Project.CUK_Compasser.domain.owner.client;

public interface NtsBusinessVerifyClient {
    boolean verify(String businessLicenseNumber, String startDate, String ownerName, String businessName);
}