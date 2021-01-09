import React, { useState, useEffect } from "react";
import Button from "react-bootstrap/Button";
import { paymentService } from "../services/payment-service";
import { useParams, useHistory } from "react-router-dom";
import Select from "react-dropdown-select";
import { toast } from "react-toastify";

const Payment = () => {
  const { orderId } = useParams();
  const history = useHistory();
  const [paymentMethods, setPaymentMethods] = useState([]);
  const [chosenPaymentMethod, setChosenPaymentMethod] = useState(null);

  const getAvailablePaymentMethods = async () => {
    try {
      const response = await paymentService.getAvailablePaymentMethods(orderId);
      setPaymentMethods(response);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  useEffect(() => {
    getAvailablePaymentMethods();
  }, []);

  const submitForm = async (event) => {
    event.preventDefault();
    try {
      const response = await paymentService.choosePaymentMethodForOrder(
        orderId,
        chosenPaymentMethod
      );
      window.location.replace(response);
    } catch (error) {
      if (error.response) {
        console.log("Error: " + JSON.stringify(error.response));
      }
      toast.error(error.response ? error.response.data : error.message, {
        hideProgressBar: true,
      });
    }
  };

  // const onClickBank = () => {
  //   paymentService.testBank(orderId);
  // };
  // const onClickPaypal = () => {
  //   payPalService.pay(orderId, "paypal");
  // };

  // const onClickBitcoin = async () => {
  //   const response = await bitcoinService.pay(orderId, "bitcoin");
  //   if ((response !== null) & (response !== undefined)) {
  //     window.location.replace(response.data); // redirection to CoinGate site
  //   } else {
  //     history.push("/bitcoin/error");
  //   }
  // };

  // const onClickAvailableServices = () => {
  //   paymentService.getAvailableServices();
  // };

  return (
    <div>
      <h1>Payment Concentrator</h1>
      <h2>Choose payment method to finish your order:</h2>
      <div className="ml-auto mr-auto" style={{ width: "40%" }}>
        <form onSubmit={submitForm}>
          <Select
            placeholder="Select payment method"
            required
            options={paymentMethods}
            style={{ marginBottom: "1em" }}
            labelField="name"
            valueField="id"
            onChange={(value) => {
              console.log(value);
              setChosenPaymentMethod(value[0].name);
            }}
          />
          <Button type="submit" variant="dark" className="myBtn">
            Pay
          </Button>
        </form>
      </div>
      {/* <Button variant="dark" className="myBtn" onClick={onClickBank}>
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
      </div> */}
    </div>
  );
};

export default Payment;
