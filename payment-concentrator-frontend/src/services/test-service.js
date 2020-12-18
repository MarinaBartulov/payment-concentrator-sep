import { HttpService } from "./http-service";
import {
  BANK_SERVICE_ENDPOINTS,
  BITCOIN_SERVICE_ENDPOINTS,
  PAYPAL_SERVICE_ENDPOINTS,
  SERVICES_ENDPOINTS,
  PSP_ENDPOINTS,
} from "../constants";

class TestService extends HttpService {
  testBank = async (orderId) => {
    try {
      console.log("order id")
      console.log(orderId)
      const { data } = await this.client.get(
        SERVICES_ENDPOINTS.PAYMENT_PROVIDER_SERVICE + PSP_ENDPOINTS.PAYMENTS + "/" + orderId
      );
      console.log(data)
      window.open(data);
      return data;
    } catch (e) {
      console.log(e);
    }
  };

  testBitcoin = async () => {
    try {
      const response = await this.client.get(
        SERVICES_ENDPOINTS.BITCOIN_PAYMENT_SERVICE +
          BITCOIN_SERVICE_ENDPOINTS.TEST
      );
      alert(response.data);
      return response;
    } catch (e) {
      console.log(e);
    }
  };

  testPayPal = async () => {
    try {
      const response = await this.client.get(
        SERVICES_ENDPOINTS.PAYPAL_PAYMENT_SERVICE +
          PAYPAL_SERVICE_ENDPOINTS.TEST
      );
      alert(response.data);
      return response;
    } catch (e) {
      console.log(e);
    }
  };

  getAvailableServices = async () => {
    try {
      const response = await this.client.get(
        SERVICES_ENDPOINTS.PAYMENT_PROVIDER_SERVICE +
          PSP_ENDPOINTS.AVAILABLE_SERVICES
      );
      alert("Available services are: " + response.data);
      console.log(response);
      return response;
    } catch (e) {
      console.log(e);
    }
  };
}

export const testService = new TestService();
