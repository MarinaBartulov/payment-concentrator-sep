import axios from "axios";

export class ApiService {
  constructor(options = {}) {
    this.client = axios.create({
      baseURL: process.env.REACT_APP_API_BASE_URL,
      ...options,
    });
    this.client.interceptors.response.use(
      this.handleSuccessResponse,
      this.handleErrorResponse
    );
    this.client.interceptors.request.use((config) => {
      console.log(sessionStorage.getItem("token"));
      const token = sessionStorage.getItem("token");
      config.headers.Authorization = token ? `Bearer ${token}` : "";
      return config;
    });
  }

  addHeader(headers) {
    this.client.defaults.headers = headers;
  }

  handleSuccessResponse(response) {
    return response;
  }

  handleErrorResponse = async (error) => {
    try {
      const { status } = error.response;
      switch (status) {
        case 401:
        case 403: {
          break;
        }
        default:
          break;
      }
      return Promise.reject(error);
    } catch (e) {
      return Promise.reject(error);
    }
  };
}

const apiService = new ApiService();

export default apiService;
