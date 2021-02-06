package team16.paymentserviceprovider.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.paymentserviceprovider.dto.AppDTO;
import team16.paymentserviceprovider.model.App;
import team16.paymentserviceprovider.model.PaymentMethod;
import team16.paymentserviceprovider.repository.AppRepository;
import team16.paymentserviceprovider.service.AppService;

import java.util.UUID;

@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private AppRepository appRepository;
    @Autowired
    private PaymentMethodServiceImpl paymentMethodService;

    Logger logger = LoggerFactory.getLogger(AppServiceImpl.class);

    @Override
    public AppDTO addNewApp(AppDTO appDTO) {

        App newApp = new App();
        newApp.setAppName(appDTO.getAppName());
        newApp.setWebAddress(appDTO.getWebAddress());
        newApp.setOfficialEmail(appDTO.getOfficialEmail());
        String appId = UUID.randomUUID().toString();
        newApp.setAppId(appId);
        for(String pmName: appDTO.getPaymentMethods()){
            PaymentMethod pm = this.paymentMethodService.findByName(pmName);
            if(pm == null){
                logger.warn("Payment method with name " + pmName + " doesn't exist.");
                return null;
            }
            newApp.getPaymentMethods().add(pm);
        }
        this.appRepository.save(newApp);
        appDTO.setAppId(appId);
        logger.info("New app successfully added. App id " + appId);
        return appDTO;
    }

    @Override
    public App findByEmail(String email) {
        return this.appRepository.findByOfficialEmail(email);
    }

    @Override
    public App findByAppId(String appId) {
        return this.appRepository.findByAppId(appId);
    }
}
