import { HttpService } from "./http-service";
import { ROUTES } from "../constants";


class AuthenticationService extends HttpService {
  authenticate = async (payload, paymentId) => {
    try {
      console.log("Payload: ")
      console.log(payload);
      console.log("Payment id:")
      console.log(paymentId)
      const response = await this.client.post(ROUTES.AUTH + "/" + paymentId, payload);
      console.log("Response: ")
      console.log(response.data);
      console.log("----------")
      window.location.replace(response.data.redirectionURL);
      return response;
    } catch (e) {
      console.log("ovde");
    }
  };

  authenticateMerchant = async (payload, metchantId) => {
    try {
      const response = await this.client.post(ROUTES.AUTH_MERCHANT + "/" + metchantId, payload);
      console.log("Response: ")
      console.log(response);
      console.log("----------")
      window.location.replace(response.data.redirectionURL);
      return response;
    } catch (e) {
      console.log("ovde");
    }
  };
}

export const authService = new AuthenticationService();
