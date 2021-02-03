import { HttpService } from "./http-service";
import { SERVICES_ENDPOINTS, PSP_ENDPOINTS } from "../constants";

class MerchantService extends HttpService {
  getMyInfo = async () => {
    const response = await this.client.get(
      SERVICES_ENDPOINTS.PAYMENT_PROVIDER_SERVICE + PSP_ENDPOINTS.MERCHANT_INFO
    );
    return response.data;
  };

  addPaymentMethodForMerchant = async (paymentMethodName, payload) => {
    const response = await this.client.post(
      SERVICES_ENDPOINTS.PAYMENT_PROVIDER_SERVICE +
        PSP_ENDPOINTS.MERCHANT_ADD_PAYMENT_METHOD +
        "/" +
        paymentMethodName,
      payload
    );
    return response.data;
  };

  createBillingPlan = async (payload) => {
    const response = await this.client.post(
      SERVICES_ENDPOINTS.PAYMENT_PROVIDER_SERVICE +
        PSP_ENDPOINTS.CREATE_BILLING_PLAN,
      payload
    );
    return response.data;
  };
}

export const merchantService = new MerchantService();
