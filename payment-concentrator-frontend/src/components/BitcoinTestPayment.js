import React from "react";
import Button from "react-bootstrap/Button";
import { bitcoinService } from "../services/bitcoin-service";

const BitcoinTestPayment = () => {
  const pay = async () => {
    const payload = {
      orderId: 1,
      email: "merchant@gmail.com",
      paymentAmount: 10,
      paymentCurrency: "EUR",
    };
    const response = await bitcoinService.pay(payload);
    window.location.replace(response.data);
  };
  return (
    <div>
      <Button onClick={pay}>Pay</Button>
    </div>
  );
};

export default BitcoinTestPayment;
