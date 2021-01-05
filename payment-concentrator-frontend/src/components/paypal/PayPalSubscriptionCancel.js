import Button from "react-bootstrap/Button";
import { useParams } from "react-router-dom";
import { useEffect } from "react";
import { payPalService } from "../../services/paypal-service";
import paypal from "../../paypal.jpg";

const PayPalSubscriptionCancel = () => {
  const { id } = useParams();

  useEffect(() => {
    payPalService.cancelSubscription(id);
  }, []);

  return (
    <div>
      <img src={paypal} alt="img" />
      <h1> Subscription canceled </h1> <br />
      <Button
        variant="btn btn-primary"
        onClick={() => window.location.replace("https://localhost:3000/error")}
      >
        {" "}
        Return{" "}
      </Button>
    </div>
  );
};

export default PayPalSubscriptionCancel;
