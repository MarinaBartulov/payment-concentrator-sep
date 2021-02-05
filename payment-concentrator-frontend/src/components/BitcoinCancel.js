import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { bitcoinService } from "../services/bitcoin-service";
import Spinner from "react-bootstrap/Spinner";
import bitcoinLogo from "../assets/bitcoin.png";
import Button from "react-bootstrap/Button";
import Header from "./Header";

const BitcoinCancel = () => {
  const { id } = useParams();
  const [cancelShow, setCancelShow] = useState(0);
  const [redirectUrl, setRedirectUrl] = useState("");

  const cancel = async (id) => {
    try {
      const response = await bitcoinService.cancel(id);
      setCancelShow(1);
      setRedirectUrl(response);
    } catch (error) {
      setCancelShow(2);
      setRedirectUrl(error.response.data);
    }
  };
  useEffect(() => {
    console.log(id);
    cancel(id);
  }, []);

  if (cancelShow === 0) {
    return (
      <div>
        <Header />
        <Spinner animation="border" variant="dark" />
      </div>
    );
  } else if (cancelShow === 1) {
    return (
      <>
        <Header />
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
            onClick={() => window.location.replace(redirectUrl)}
          >
            Return
          </Button>
        </div>
      </>
    );
  } else {
    return (
      <div>
        <Header />
        <h2 style={{ color: "Red" }}>Something went wrong</h2>
        <Button
          style={{ marginTop: "1em" }}
          onClick={() => window.location.replace(redirectUrl)}
        >
          Return
        </Button>
      </div>
    );
  }
};

export default BitcoinCancel;
