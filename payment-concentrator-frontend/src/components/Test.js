import React from "react";
import Button from "react-bootstrap/Button";
import { testService } from "../services/test-service";
import { useParams } from "react-router-dom";

const Test = () => {

  const { orderId } = useParams();

  const onClickBank = () => {
    testService.testBank(orderId);
  };
  const onClickPaypal = () => {
    testService.testPayPal();
  };

  const onClickBitcoin = () => {
    testService.testBitcoin();
  };

  const onClickAvailableServices = () => {
    testService.getAvailableServices();
  };

  return (
    <div>
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
