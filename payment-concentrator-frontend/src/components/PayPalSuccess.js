import Button from "react-bootstrap/Button";
import paypal from "../paypal.jpg";

const PayPalSuccess = () => {
  return (
    <div>
      <img src={paypal} alt="img" />
      <h1> Payment success </h1> <br />
      <Button variant="btn btn-danger" onClick={() => window.close()}>
        {" "}
        Close{" "}
      </Button>
    </div>
  );
};

export default PayPalSuccess;
