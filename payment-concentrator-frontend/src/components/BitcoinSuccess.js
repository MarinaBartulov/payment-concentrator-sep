import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import successPhoto from "../assets/successPhoto.png";
import { bitcoinService } from "../services/bitcoin-service";
import Spinner from "react-bootstrap/Spinner";

const BitcoinSuccess = () => {
  const { id } = useParams();
  const [successShow, setSuccessShow] = useState(0);

  const success = async (id) => {
    const response = await bitcoinService.success(id);
    if (response != null) {
      setSuccessShow(1);
    } else {
      setSuccessShow(2);
    }
  };
  useEffect(() => {
    console.log(id);
    success(id);
  }, []);

  if (successShow === 0) {
    return (
      <div>
        <Spinner animation="border" variant="dark" />
      </div>
    );
  } else if (successShow === 1) {
    return (
      <>
        <div
          style={{
            backgroundColor: "rgb(55, 255, 37)",
            display: "inline-block",
            marginTop: "2em",
          }}
        >
          <span>
            <h2 style={{ color: "black", padding: "1em" }}>
              Your Bitcoin payment was successful!
            </h2>
          </span>
        </div>
        <div style={{ display: "block" }}>
          <img src={successPhoto} alt="success" width="350em" height="300em" />
        </div>
      </>
    );
  } else {
    return <div style={{ color: "Red" }}>Something went wrong</div>;
  }
};

export default BitcoinSuccess;
