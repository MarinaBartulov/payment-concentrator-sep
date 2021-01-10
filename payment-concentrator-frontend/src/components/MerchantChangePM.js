import React, { useEffect } from "react";
import Header from "./Header";
import { merchantService } from "../services/merchant-service";
import { useHistory } from "react-router-dom";

const MerchantChangePM = () => {
  const history = useHistory();

  const getMerchantInfo = async () => {
    const response = await merchantService.getMyInfo();
    if (!response.pmChosen) {
      history.push("/merchantChoosePM");
    }
  };

  useEffect(() => {
    getMerchantInfo();
  }, []);

  return (
    <div>
      <Header />
      <h2>Change your payment methods:</h2>
    </div>
  );
};

export default MerchantChangePM;
