import { useEffect } from "react";
import { useLocation, useParams } from "react-router-dom";
import queryString from "query-string";
import { payPalService } from "../../services/paypal-service";
import Spinner from "../Spinner";

const PayPalSubscriptionReturn = () => {
  const { id } = useParams();
  const { search } = useLocation();
  const values = queryString.parse(search);
  const token = values.token;

  useEffect(() => {
    payPalService.executeSubscription(id, token);
  }, []);
  return <Spinner />;
};

export default PayPalSubscriptionReturn;
