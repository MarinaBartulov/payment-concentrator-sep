import { HttpService } from "./http-service";
import { SERVICES_ENDPOINTS, PSP_ENDPOINTS } from "../constants";

class OrderService extends HttpService {
  getAvailablePaymentMethods = async (id) => {
    const response = await this.client.get(
      SERVICES_ENDPOINTS.PAYMENT_PROVIDER_SERVICE +
        PSP_ENDPOINTS.ORDER +
        "/" +
        id +
        "/paymentMethods"
    );
    return response.data;
  };

  choosePaymentMethodForOrder = async (id, paymentMethod) => {
    const response = await this.client.put(
      SERVICES_ENDPOINTS.PAYMENT_PROVIDER_SERVICE +
        PSP_ENDPOINTS.ORDER +
        "/" +
        id +
        "/choosePaymentMethod",
      paymentMethod
    );
    return response.data;
  };
}

export const orderService = new OrderService();
