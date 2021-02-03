import React, { useState } from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import Button from "react-bootstrap/Button";
import { useHistory } from "react-router-dom";

const Header = () => {
  const [loggedIn, setLoggedIn] = useState(
    sessionStorage.getItem("token") !== "null" &&
      sessionStorage.getItem("token") !== null
  );
  const [role, setRole] = useState(sessionStorage.getItem("role"));
  const history = useHistory();

  const goToAdminPanel = () => {
    history.push("/adminPanel");
  };
  const goToLogin = () => {
    history.push("/login");
  };

  const goToBillingPlan = () => {
    history.push("/billingPlan");
  };

  const goToChangePM = () => {
    history.push("/merchantChangePM");
  };

  return (
    <div>
      <Navbar bg="primary" variant="dark">
        <Navbar.Brand href="/">Payment Concentrator</Navbar.Brand>
        <Nav className="mr-auto" style={{ width: "100%" }}>
          {loggedIn && role === "ROLE_MERCHANT" && (
            <Nav.Item
              className="ml-2"
              style={{ backgroundColor: "black", borderRadius: "10px" }}
            >
              <Button
                style={{ color: "white" }}
                variant="link"
                onClick={goToBillingPlan}
              >
                Billing Plan
              </Button>
            </Nav.Item>
          )}
          {loggedIn && role === "ROLE_MERCHANT" && (
            <Nav.Item
              className="ml-2"
              style={{ backgroundColor: "black", borderRadius: "10px" }}
            >
              <Button
                style={{ color: "white" }}
                variant="link"
                onClick={goToChangePM}
              >
                Payment Methods
              </Button>
            </Nav.Item>
          )}
        </Nav>

        <Nav className="justify-content-end" style={{ width: "100%" }}>
          {role === "ROLE_ADMIN" && loggedIn && (
            <Nav.Item
              className="ml-2"
              style={{ backgroundColor: "black", borderRadius: "10px" }}
            >
              <Button
                style={{ color: "white" }}
                variant="link"
                onClick={goToAdminPanel}
              >
                Admin panel
              </Button>
            </Nav.Item>
          )}

          {!loggedIn && (
            <Nav.Item
              className="ml-2"
              style={{ backgroundColor: "black", borderRadius: "10px" }}
            >
              <Button
                style={{ color: "white" }}
                variant="link"
                onClick={goToLogin}
              >
                Login
              </Button>
            </Nav.Item>
          )}
          {loggedIn && (
            <Nav.Item
              className="ml-2"
              style={{ backgroundColor: "black", borderRadius: "10px" }}
            >
              <Button
                style={{ color: "white" }}
                variant="link"
                onClick={() => {
                  sessionStorage.setItem("token", null);
                  sessionStorage.setItem("role", null);
                  goToLogin();
                }}
              >
                Logout
              </Button>
            </Nav.Item>
          )}
        </Nav>
      </Navbar>
    </div>
  );
};

export default Header;
