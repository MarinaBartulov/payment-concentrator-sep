import React, { useState, useEffect } from "react";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import { orderService } from "../services/order-service";
import { useParams, useHistory } from "react-router-dom";
import Select from "react-dropdown-select";
import { toast } from "react-toastify";
import Header from "./Header";

const Payment = () => {
  const { orderId } = useParams();
  const history = useHistory();
  const [paymentMethods, setPaymentMethods] = useState([]);
  const [chosenPaymentMethod, setChosenPaymentMethod] = useState(null);

  const getAvailablePaymentMethods = async () => {
    try {
      const response = await orderService.getAvailablePaymentMethods(orderId);
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
      const response = await orderService.choosePaymentMethodForOrder(
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

  return (
    <div>
      <Header />
      <h1>Payment Concentrator</h1>
      <Card
        body
        style={{
          margin: "auto",
          backgroundColor: "#cdf2f7",
          width: "60%",
        }}
      >
        <h2>Choose payment method to finish your order:</h2>
        <div className="ml-auto mr-auto" style={{ width: "60%" }}>
          <form onSubmit={submitForm}>
            <Select
              placeholder="Select payment method"
              required
              options={paymentMethods}
              style={{ marginBottom: "1em", backgroundColor: "white" }}
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
      </Card>
    </div>
  );
};

export default Payment;
