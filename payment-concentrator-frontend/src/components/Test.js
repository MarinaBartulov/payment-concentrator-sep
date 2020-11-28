import React from "react";
import Button from "react-bootstrap/Button";
import { testService } from "../services/test-service";

const Test = () => {
  const onClickBank = () => {
    testService.test();
  };
  const onClickPaypal = () => {};

  const onClickBitcoin = () => {};

  return (
    <div>
      <h2>Payment methods</h2>
      <Button variant="dark" onClick={onClickBank}>
        Bank
      </Button>
      <Button variant="dark" onClick={onClickPaypal}>
        Paypal
      </Button>
      <Button variant="dark" onClick={onClickBitcoin}>
        Bitcoin
      </Button>
    </div>
  );
};

export default Test;
