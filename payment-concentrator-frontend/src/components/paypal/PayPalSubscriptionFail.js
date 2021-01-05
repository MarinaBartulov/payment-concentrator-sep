import Button from "react-bootstrap/Button";
import paypal from "../../paypal.jpg";

const PayPalSubscriptionFail = () => {
  return (
    <div>
      <img src={paypal} alt="img" />
      <h1> Subscription failed </h1> <br />
      <Button
        variant="btn btn-primary"
        onClick={() => window.location.replace("https://localhost:3000/failed")}
      >
        {" "}
        Return{" "}
      </Button>
    </div>
  );
};

export default PayPalSubscriptionFail;
