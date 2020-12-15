import React from "react";
import Button from "react-bootstrap/Button";
import { testService } from "../services/test-service";
import { payPalService } from "../services/paypal-service";

const Test = () => {
  const onClickBank = () => {
    testService.testBank();
  };
  const onClickPaypal = () => {
    //testService.testPayPal();
    payPalService.pay(payPalData);
  };

  const onClickBitcoin = () => {
    testService.testBitcoin();
  };

  const onClickAvailableServices = () => {
    testService.getAvailableServices();
  };

  const payPalData = {
    price: 1,
    currency: "USD",
    method: "paypal",
    intent: "sale",
    description: "test",
  };

  return (
    <div>
      <h1>Payment Concentrator</h1>
      <h2>Payment methods</h2>
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

export default Test;
