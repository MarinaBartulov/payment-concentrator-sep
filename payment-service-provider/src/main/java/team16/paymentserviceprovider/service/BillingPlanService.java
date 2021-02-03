package team16.paymentserviceprovider.service;

import team16.paymentserviceprovider.dto.BillingPlanDTO;
import team16.paymentserviceprovider.model.BillingPlan;

import java.util.List;

public interface BillingPlanService {

    BillingPlan getOne(Long id);
    List<BillingPlanDTO> getAllBillingPlansForMerchant(Long id);
}
