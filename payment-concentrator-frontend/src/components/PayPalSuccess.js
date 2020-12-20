import Button from "react-bootstrap/Button";
import paypal from "../paypal.jpg";

const PayPalSuccess = () => {
  return (
    <div>
      <img src={paypal} alt="img" />
      <h1> Payment success </h1> <br />
      <Button
        variant="btn btn-primary"
        onClick={() =>
          window.location.replace("https://localhost:3000/success")
        }
      >
        {" "}
        Return{" "}
      </Button>
    </div>
  );
};

export default PayPalSuccess;
