import Button from "react-bootstrap/Button";
import { useParams } from "react-router-dom";
import { useEffect } from "react";
import { payPalService } from "../services/paypal-service";
import paypal from "../paypal.jpg";

const PayPalCancel = () => {
  const { id } = useParams();

  useEffect(() => {
    payPalService.cancelPayment(id);
  }, []);

  return (
    <div>
      <img src={paypal} alt="img" />
      <h1> Payment canceled </h1> <br />
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

export default PayPalCancel;
