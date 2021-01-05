package team16.paymentserviceprovider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.paymentserviceprovider.model.BillingPlan;
import team16.paymentserviceprovider.repository.BillingPlanRepository;
import team16.paymentserviceprovider.service.BillingPlanService;

@Service
public class BillingPlanServiceImpl implements BillingPlanService {

    @Autowired
    private BillingPlanRepository billingPlanRepository;

    @Override
    public BillingPlan getOne(Long id) {
        return billingPlanRepository.getOne(id);
    }
}
