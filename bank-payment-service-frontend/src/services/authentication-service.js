import { HttpService } from "./http-service";
import { ROUTES } from "../constants";


class AuthenticationService extends HttpService {
  authenticate = async (payload, paymentId) => {
     const response = await this.client.post(ROUTES.AUTH + "/" + paymentId, payload);
    return response.data;
  };

  authenticateMerchant = async (payload, metchantId) => {
    const response = await this.client.post(ROUTES.AUTH_MERCHANT + "/" + metchantId, payload);
    return response.data;
    } 
}

export const authService = new AuthenticationService();
