package team16.paymentserviceprovider.service;

import team16.paymentserviceprovider.dto.AppDTO;
import team16.paymentserviceprovider.model.App;

public interface AppService {

      String addNewApp(AppDTO appDTO);
      App findByEmail(String email);
}
