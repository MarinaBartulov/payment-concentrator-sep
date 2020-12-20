import { useEffect } from "react";
import { useLocation, useHistory } from "react-router-dom";
import queryString from "query-string";
import { payPalService } from "../services/paypal-service";
import Spinner from "./Spinner";

const PayPalReturn = () => {
  let history = useHistory();
  const { search } = useLocation();
  const values = queryString.parse(search);
  const paymentId = values.paymentId;
  const PayerID = values.PayerID;

  useEffect(() => {
    payPalService.executePayment(paymentId, PayerID);
    /*.then(() => {
      if (response != null && response.data === "approved") {
        history.push("/pay/return/success");
      } else {
        history.push("/pay/return/fail");
      }
    })*/
  }, []);
  return <Spinner />;
};

export default PayPalReturn;
