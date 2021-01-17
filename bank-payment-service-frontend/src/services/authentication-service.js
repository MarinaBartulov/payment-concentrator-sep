import { HttpService } from "./http-service";
import { ROUTES } from "../constants";


class AuthenticationService extends HttpService {
  authenticate = async (payload, paymentId) => {
    try {
      const response = await this.client.post(ROUTES.AUTH + "/" + paymentId, payload);
      window.location.replace(response.data.redirectionURL);
      return response;
    } catch (e) {
      console.log("ovde");
    }
  };

  authenticateMerchant = async (payload, metchantId) => {
    const response = await this.client.post(ROUTES.AUTH_MERCHANT + "/" + metchantId, payload);
    return response.data;
    } 
}

export const authService = new AuthenticationService();
