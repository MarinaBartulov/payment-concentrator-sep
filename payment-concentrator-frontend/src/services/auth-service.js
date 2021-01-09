import { HttpService } from "./http-service";
import { SERVICES_ENDPOINTS, PSP_ENDPOINTS } from "../constants";

class AuthService extends HttpService {
  login = async (payload) => {
    const response = await this.client.post(
      SERVICES_ENDPOINTS.PAYMENT_PROVIDER_SERVICE + PSP_ENDPOINTS.AUTH_LOGIN,
      payload
    );
    return response.data;
  };

  changePassword = async (payload) => {
    const response = await this.client.post(
      SERVICES_ENDPOINTS.PAYMENT_PROVIDER_SERVICE +
        PSP_ENDPOINTS.AUTH_CHANGE_PASSWORD,
      payload
    );
    return response.data;
  };
}

export const authService = new AuthService();
