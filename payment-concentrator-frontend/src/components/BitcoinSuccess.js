import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { bitcoinService } from "../services/bitcoin-service";
import Spinner from "react-bootstrap/Spinner";
import bitcoinLogo from "../assets/bitcoin.png";
import Button from "react-bootstrap/Button";

const BitcoinSuccess = () => {
  const { id } = useParams();
  const [successShow, setSuccessShow] = useState(0);

  const success = async (id) => {
    const response = await bitcoinService.success(id);
    if (response != null && response != undefined) {
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
        <div style={{ display: "block", marginTop: "1em" }}>
          <img src={bitcoinLogo} alt="error" width="300em" height="300em" />
          <br></br>
          <Button
            style={{ marginTop: "1em" }}
            onClick={() =>
              window.location.replace("https://localhost:3000/success")
            }
          >
            Return
          </Button>
        </div>
      </>
    );
  } else {
    return <div style={{ color: "Red" }}>Something went wrong</div>;
  }
};

export default BitcoinSuccess;
