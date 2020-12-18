import React from "react";
import Card from "react-bootstrap/Card";

const Home = () => {


  return (
    <div className="homeDiv">
      <Card
        body
        style={{
          width: "25rem",
          margin: "auto",
          backgroundColor: "white",
        }}
      >
        <h1>Welcome to UniCredit Bank</h1>
      </Card>
    </div>
  );
};

export default Home;
