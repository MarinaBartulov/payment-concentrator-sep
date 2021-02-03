package team16.paymentserviceprovider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.paymentserviceprovider.dto.BillingPlanDTO;
import team16.paymentserviceprovider.model.BillingPlan;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.repository.BillingPlanRepository;
import team16.paymentserviceprovider.service.BillingPlanService;

import java.util.ArrayList;
import java.util.List;

@Service
public class BillingPlanServiceImpl implements BillingPlanService {

    @Autowired
    private BillingPlanRepository billingPlanRepository;

    @Override
    public BillingPlan getOne(Long id) {
        return billingPlanRepository.getOne(id);
    }

    @Override
    public BillingPlan save(BillingPlan billingPlan){
        return billingPlanRepository.save(billingPlan);
    }

    @Override
    public List<BillingPlanDTO> getAllBillingPlansForMerchant(Long id) {

        List<BillingPlanDTO> billingPlansDTO = new ArrayList<>();

        List<BillingPlan> merchantBillingPlans = billingPlanRepository.findAllByMerchant(id);
        System.out.println("Merchant billing plans: " + merchantBillingPlans.size());

        for(BillingPlan bp : merchantBillingPlans)
        {
            BillingPlanDTO billingPlanDTO = new BillingPlanDTO(bp);
            billingPlansDTO.add(billingPlanDTO);
        }
        System.out.println("Merchant billing plans: " + billingPlansDTO.size());
        return billingPlansDTO;
    }

    @Override
    public BillingPlan create(BillingPlanDTO billingPlanDTO, Merchant merchant) {

        BillingPlan billingPlan = new BillingPlan(billingPlanDTO, merchant);
        return save(billingPlan);
    }
}
