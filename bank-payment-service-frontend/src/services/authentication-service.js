import { HttpService } from "./http-service";
import { ROUTES } from "../constants";

class AuthenticationService extends HttpService {
  authenticate = async (payload) => {
    try {
      const response = await this.client.post(ROUTES.AUTH, payload);
      //console.log(response);
      return response;
    } catch (e) {
      console.log(e);
    }
  };
}

export const authService = new AuthenticationService();
