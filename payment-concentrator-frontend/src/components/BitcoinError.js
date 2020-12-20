import React from "react";
import bitcoinLogo from "../assets/bitcoin.png";
import Button from "react-bootstrap/Button";

const BitcoinError = () => {
  return (
    <>
      <div
        style={{
          backgroundColor: "rgb(248, 0, 12)",
          display: "inline-block",
          marginTop: "2em",
        }}
      >
        <span style={{ color: "black", padding: "1em" }}>
          <h2>Error!</h2>
          <h3>Your Bitcoin payment was unsuccessful!</h3>
        </span>
      </div>
      <div style={{ display: "block", marginTop: "1em" }}>
        <img src={bitcoinLogo} alt="error" width="300em" height="300em" />
        <br></br>
        <Button
          style={{ marginTop: "1em" }}
          onClick={() =>
            window.location.replace("https://localhost:3000/error")
          }
        >
          Return
        </Button>
      </div>
    </>
  );
};

export default BitcoinError;
