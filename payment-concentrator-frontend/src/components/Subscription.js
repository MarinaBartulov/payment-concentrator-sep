import React from "react";
import Button from "react-bootstrap/Button";
import { payPalService } from "../services/paypal-service";
import { useParams } from "react-router-dom";
import Header from "./Header";

const Subscription = () => {
  const { subscriptionId } = useParams();

  const onClickPaypal = () => {
    payPalService.createSubscription(subscriptionId);
  };

  return (
    <div>
      <Header />
      <h1>Payment Concentrator</h1>
      <h2>Subscription methods</h2>
      <Button variant="dark" className="myBtn" onClick={onClickPaypal}>
        Paypal
      </Button>
    </div>
  );
};

export default Subscription;
