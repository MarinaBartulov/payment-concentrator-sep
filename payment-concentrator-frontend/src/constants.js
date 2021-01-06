export const ROUTES = {};

export const SERVICES_ENDPOINTS = {
  BANK_PAYMENT_SERVICE: "/bank-payment-service",
  PAYPAL_PAYMENT_SERVICE: "/paypal-payment-service",
  BITCOIN_PAYMENT_SERVICE: "/bitcoin-payment-service",
  PAYMENT_PROVIDER_SERVICE: "/psp-service/api",
};

export const BANK_SERVICE_ENDPOINTS = {
  TEST: "/api/test",
};

export const PAYPAL_SERVICE_ENDPOINTS = {
  TEST: "/api/test",
  PAY: "/api/pay/",
  EXECUTE: "/api/pay/execute",
  CANCEL: "/api/pay/cancel/",
};

export const BITCOIN_SERVICE_ENDPOINTS = {
  TEST: "/api/test",
};

export const PSP_ENDPOINTS = {
  AVAILABLE_SERVICES: "/test/available-services",
  PAYMENTS: "/payments",
  APP: "/app",
};
