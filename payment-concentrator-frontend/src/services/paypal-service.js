import { HttpService } from "./http-service";
import {
  SERVICES_ENDPOINTS,
  PAYPAL_SERVICE_ENDPOINTS,
  PSP_ENDPOINTS,
} from "../constants";

class PayPalService extends HttpService {
  pay = async (orderId, mode) => {
    try {
      const response = await this.client.put(
        SERVICES_ENDPOINTS.PAYMENT_PROVIDER_SERVICE +
          PSP_ENDPOINTS.PAYMENTS +
          "/" +
          orderId,
        mode
      );
      window.location.replace(response.data); // redirection to PayPal site or to fail page
      return response;
    } catch (e) {
      console.log(e);
    }
  };

  executePayment = async (paymentId, PayerID) => {
    try {
      const response = await this.client.get(
        SERVICES_ENDPOINTS.PAYPAL_PAYMENT_SERVICE +
          PAYPAL_SERVICE_ENDPOINTS.EXECUTE +
          `?paymentId=${paymentId}&PayerID=${PayerID}`
      );
      window.location.replace(response.data);
      return response;
    } catch (e) {
      console.log(e);
      return null;
    }
  };

  cancelPayment = async (id) => {
    try {
      const response = await this.client.get(
        SERVICES_ENDPOINTS.PAYPAL_PAYMENT_SERVICE +
          PAYPAL_SERVICE_ENDPOINTS.CANCEL +
          `?id=${id}`
      );
      return response;
    } catch (e) {
      console.log(e);
      return null;
    }
  };
}

export const payPalService = new PayPalService();
