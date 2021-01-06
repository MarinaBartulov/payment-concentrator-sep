import { HttpService } from "./http-service";
import { SERVICES_ENDPOINTS, PSP_ENDPOINTS } from "../constants";

class AppService extends HttpService {
  addNewApp = async (payload) => {
    const response = await this.client.post(
      SERVICES_ENDPOINTS.PAYMENT_PROVIDER_SERVICE + PSP_ENDPOINTS.APP,
      payload
    );
    console.log(response.data);
    return response.data;
  };
}

export const appService = new AppService();
