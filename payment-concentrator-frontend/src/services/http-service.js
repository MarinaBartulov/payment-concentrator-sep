import apiService from "./api-service";

export class HttpService {
  constructor() {
    this.apiService = apiService;
    this.client = this.apiService.client;
  }
}
