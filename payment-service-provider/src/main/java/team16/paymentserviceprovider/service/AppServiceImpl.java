package team16.paymentserviceprovider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.paymentserviceprovider.dto.AppDTO;
import team16.paymentserviceprovider.model.App;
import team16.paymentserviceprovider.repository.AppRepository;

import java.util.UUID;

@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private AppRepository appRepository;

    @Override
    public String addNewApp(AppDTO appDTO) {

        App newApp = new App();
        newApp.setAppName(appDTO.getAppName());
        newApp.setWebAddress(appDTO.getWebAddress());
        newApp.setOfficialEmail(appDTO.getOfficialEmail());
        String appId = UUID.randomUUID().toString();
        System.out.println(appId);
        newApp.setAppId(appId);
        this.appRepository.save(newApp);
        return appId;
    }

    @Override
    public App findByEmail(String email) {
        return this.appRepository.findByOfficialEmail(email);
    }
}
