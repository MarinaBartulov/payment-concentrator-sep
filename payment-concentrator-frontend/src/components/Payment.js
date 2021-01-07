import React from "react";
import Button from "react-bootstrap/Button";
import { testService } from "../services/test-service";
import { payPalService } from "../services/paypal-service";
import { bitcoinService } from "../services/bitcoin-service";
import { useParams, useHistory } from "react-router-dom";

const Payment = () => {
  const { orderId } = useParams();
  const history = useHistory();

  const onClickBank = () => {
    testService.testBank(orderId);
  };
  const onClickPaypal = () => {
    payPalService.pay(orderId, "paypal");
  };

  const onClickBitcoin = async () => {
    const response = await bitcoinService.pay(orderId, "bitcoin");
    if ((response !== null) & (response !== undefined)) {
      window.location.replace(response.data); // redirection to CoinGate site
    } else {
      history.push("/bitcoin/error");
    }
  };

  const onClickAvailableServices = () => {
    testService.getAvailableServices();
  };

  return (
    <div>
      <h1>Payment Concentrator</h1>
      <h2>Choose payment method to finish your order:</h2>
      <Button variant="dark" className="myBtn" onClick={onClickBank}>
        Bank
      </Button>
      <Button variant="dark" className="myBtn" onClick={onClickPaypal}>
        Paypal
      </Button>
      <Button variant="dark" className="myBtn" onClick={onClickBitcoin}>
        Bitcoin
      </Button>
      <div>
        <Button className="btnAvailable" onClick={onClickAvailableServices}>
          Available Payment Services
        </Button>
      </div>
    </div>
  );
};

export default Payment;
