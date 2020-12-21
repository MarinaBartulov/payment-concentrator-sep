import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { bitcoinService } from "../services/bitcoin-service";
import Spinner from "react-bootstrap/Spinner";
import bitcoinLogo from "../assets/bitcoin.png";
import Button from "react-bootstrap/Button";

const BitcoinCancel = () => {
  const { id } = useParams();
  const [cancelShow, setCancelShow] = useState(0);

  const cancel = async (id) => {
    const response = await bitcoinService.cancel(id);
    if (response != null && response != undefined) {
      setCancelShow(1);
    } else {
      setCancelShow(2);
    }
  };
  useEffect(() => {
    console.log(id);
    cancel(id);
  }, []);

  if (cancelShow === 0) {
    return (
      <div>
        <Spinner animation="border" variant="dark" />
      </div>
    );
  } else if (cancelShow === 1) {
    return (
      <>
        <div
          style={{
            backgroundColor: "rgb(248, 0, 12)",
            display: "inline-block",
            marginTop: "2em",
          }}
        >
          <span>
            <h2 style={{ color: "black", padding: "1em", paddingBottom: "0" }}>
              Your Bitcoin payment failed!
            </h2>
            <h3>(canceled, expired or invalid)</h3>
          </span>
        </div>
        <div style={{ display: "block", marginTop: "1em" }}>
          <img src={bitcoinLogo} alt="error" width="300em" height="300em" />
          <br></br>
          <Button
            style={{ marginTop: "1em" }}
            onClick={() =>
              window.location.replace("https://localhost:3000/failed")
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

export default BitcoinCancel;
