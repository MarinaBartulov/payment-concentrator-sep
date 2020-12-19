import React from "react";
import cancelPhoto from "../assets/cancelPhoto.png";

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
      <div style={{ display: "block" }}>
        <img src={cancelPhoto} alt="cancel" width="300em" height="300em" />
      </div>
    </>
  );
};

export default BitcoinError;
