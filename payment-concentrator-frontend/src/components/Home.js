import React from "react";
import Card from "react-bootstrap/Card";

const Home = () => {
  return (
    <div className="homeDiv">
      <Card
        body
        style={{
          width: "40rem",
          margin: "auto",
          backgroundColor: "#cdf2f7",
        }}
      >
        <h1>Welcome to Payment Concentrator</h1>
      </Card>
    </div>
  );
};

export default Home;
