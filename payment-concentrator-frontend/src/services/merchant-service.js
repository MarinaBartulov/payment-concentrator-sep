import { HttpService } from "./http-service";
import { SERVICES_ENDPOINTS, PSP_ENDPOINTS } from "../constants";

class MerchantService extends HttpService {
  getMyInfo = async () => {
    const response = await this.client.get(
      SERVICES_ENDPOINTS.PAYMENT_PROVIDER_SERVICE + PSP_ENDPOINTS.MERCHANT_INFO
    );
    return response.data;
  };
}

export const merchantService = new MerchantService();
