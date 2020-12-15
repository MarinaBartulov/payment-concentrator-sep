import { HttpService } from "./http-service";
import { SERVICES_ENDPOINTS, PAYPAL_SERVICE_ENDPOINTS } from "../constants";

class PayPalService extends HttpService {
  pay = async (payload) => {
    try {
      const response = await this.client.post(
        SERVICES_ENDPOINTS.PAYPAL_PAYMENT_SERVICE +
          PAYPAL_SERVICE_ENDPOINTS.PAY,
        payload
      );
      window.open(response.data); // redirection to PayPal site
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
      return response;
    } catch (e) {
      console.log(e);
      return null;
    }
  };
}

export const payPalService = new PayPalService();
