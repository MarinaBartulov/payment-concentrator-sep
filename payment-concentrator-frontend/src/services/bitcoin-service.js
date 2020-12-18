import { HttpService } from "./http-service";

class BitcoinService extends HttpService {
  pay = async (payload) => {
    try {
      const response = await this.client.post(
        "/bitcoin-payment-service/create",
        payload
      );
      return response;
    } catch (e) {
      console.log(e);
    }
  };

  success = async (id) => {
    try {
      const response = await this.client.get(
        "/bitcoin-payment-service/success?id=" + id
      );
      return response;
    } catch (e) {
      console.log(e);
    }
  };

  cancel = async (id) => {
    try {
      const response = await this.client.get(
        "/bitcoin-payment-service/cancel?id=" + id
      );
      return response;
    } catch (e) {
      console.log(e);
    }
  };
}

export const bitcoinService = new BitcoinService();
