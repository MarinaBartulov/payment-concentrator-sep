import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class AuthService extends HttpService {
  login = async (payload) => {
    const response = await this.client.post(ROUTES.AUTH_LOGIN, payload);
    return response.data;
  };

  logout = () => {
    localStorage.setItem("token", null);
  };
}

export const authService = new AuthService();
