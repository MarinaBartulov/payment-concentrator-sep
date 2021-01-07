import React from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import Button from "react-bootstrap/Button";
import { useHistory } from "react-router-dom";

const Header = () => {
  const history = useHistory();

  const goToRegisterApp = () => {
    history.push("/appRegistration");
  };
  return (
    <div>
      <Navbar bg="primary" variant="dark">
        <Navbar.Brand href="/">Payment Concentrator</Navbar.Brand>
        <Nav className="mr-auto">
          <Button
            style={{ color: "white" }}
            className="ml-2"
            variant="link"
            onClick={goToRegisterApp}
          >
            Register app
          </Button>
        </Nav>
      </Navbar>
    </div>
  );
};

export default Header;
