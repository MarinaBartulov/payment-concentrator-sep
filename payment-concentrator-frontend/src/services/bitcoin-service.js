import { HttpService } from "./http-service";
import {
  SERVICES_ENDPOINTS,
  BITCOIN_SERVICE_ENDPOINTS,
  PSP_ENDPOINTS,
} from "../constants";

class BitcoinService extends HttpService {
  pay = async (orderId, mode) => {
    try {
      const response = await this.client.put(
        SERVICES_ENDPOINTS.PAYMENT_PROVIDER_SERVICE +
          PSP_ENDPOINTS.PAYMENTS +
          "/" +
          orderId,
        mode
      );
      window.location.replace(response.data); // redirection to CoinGate site
      return response;
    } catch (e) {
      console.log(e);
      alert("Error!");
    }
  };

  success = async (id) => {
    try {
      const response = await this.client.get(
        "/bitcoin-payment-service/api/success?id=" + id
      );
      return response;
    } catch (e) {
      console.log(e);
    }
  };

  cancel = async (id) => {
    try {
      const response = await this.client.get(
        "/bitcoin-payment-service/api/cancel?id=" + id
      );
      return response;
    } catch (e) {
      console.log(e);
    }
  };
}

export const bitcoinService = new BitcoinService();
