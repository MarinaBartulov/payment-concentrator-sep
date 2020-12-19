import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import cancelPhoto from "../assets/cancelPhoto.png";
import { bitcoinService } from "../services/bitcoin-service";
import Spinner from "react-bootstrap/Spinner";

const BitcoinCancel = () => {
  const { id } = useParams();
  const [cancelShow, setCancelShow] = useState(0);

  const cancel = async (id) => {
    const response = await bitcoinService.cancel(id);
    if (response != null) {
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
            <h2 style={{ color: "black", padding: "1em" }}>
              Your Bitcoin payment was canceled!
            </h2>
          </span>
        </div>
        <div style={{ display: "block" }}>
          <img src={cancelPhoto} alt="cancel" width="300em" height="300em" />
        </div>
      </>
    );
  } else {
    return <div style={{ color: "Red" }}>Something went wrong</div>;
  }
};

export default BitcoinCancel;
