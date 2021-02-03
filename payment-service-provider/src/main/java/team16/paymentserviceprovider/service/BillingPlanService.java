package team16.paymentserviceprovider.service;

import team16.paymentserviceprovider.dto.BillingPlanDTO;
import team16.paymentserviceprovider.model.BillingPlan;
import team16.paymentserviceprovider.model.Merchant;

import java.util.List;

public interface BillingPlanService {

    BillingPlan getOne(Long id);
    List<BillingPlanDTO> getAllBillingPlansForMerchant(Long id);
    BillingPlan create(BillingPlanDTO billingPlanDTO, Merchant merchant);
    BillingPlan save(BillingPlan billingPlan);
}
