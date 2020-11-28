package team16.paymentserviceprovider.service;

import com.netflix.appinfo.InstanceInfo;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestService {

    @Qualifier("eurekaClient")
    @Autowired
    private EurekaClient discoveryClient;

    public List<String> getAvailableServices() {
        List<Application> applications = discoveryClient.getApplications().getRegisteredApplications();
        List<String> appsNames = new ArrayList<String>();
        for (Application application : applications) {
            List<InstanceInfo> applicationsInstances = application.getInstances();
            for (InstanceInfo applicationsInstance : applicationsInstances) {

                String name = applicationsInstance.getAppName();
                appsNames.add(name);
                String url = applicationsInstance.getHomePageUrl();
            }
        }

        //appsNames.stream().filter(a -> a.contains("PAYMENT")).collect(Collectors.toList());

        return appsNames.stream().filter(a -> a.contains("PAYMENT")).collect(Collectors.toList());
    }
}
