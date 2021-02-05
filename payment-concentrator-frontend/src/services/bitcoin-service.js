import { HttpService } from "./http-service";
import {
  SERVICES_ENDPOINTS,
  BITCOIN_SERVICE_ENDPOINTS,
  PSP_ENDPOINTS,
} from "../constants";

class BitcoinService extends HttpService {
  success = async (id) => {
    const response = await this.client.get(
      "/bitcoin-payment-service/api/success?id=" + id
    );
    return response.data;
  };

  cancel = async (id) => {
    const response = await this.client.get(
      "/bitcoin-payment-service/api/cancel?id=" + id
    );
    return response.data;
  };
}

export const bitcoinService = new BitcoinService();
