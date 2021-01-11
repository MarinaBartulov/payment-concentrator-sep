import { HttpService } from "./http-service";
import { SERVICES_ENDPOINTS, PSP_ENDPOINTS } from "../constants";

class PaymentMethodService extends HttpService {
  getFormsDataForAvailablePaymentMethods = async () => {
    const response = await this.client.get(
      SERVICES_ENDPOINTS.PAYMENT_PROVIDER_SERVICE +
        PSP_ENDPOINTS.PAYMENT_METHOD_FORMS_DATA
    );
    return response.data;
  };

  getAllPaymentMethods = async () => {
    const response = await this.client.get(
      SERVICES_ENDPOINTS.PAYMENT_PROVIDER_SERVICE + PSP_ENDPOINTS.PAYMENT_METHOD
    );
    return response.data;
  };
}

export const paymentMethodService = new PaymentMethodService();
