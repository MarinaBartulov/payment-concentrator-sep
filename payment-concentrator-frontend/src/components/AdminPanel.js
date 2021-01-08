import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import AppRegistration from "./AppRegistration";
import Header from "./Header";

const AdminPanel = () => {
  const [showAppReg, setShowAppReg] = useState(false);

  return (
    <div>
      <Header />
      <h2>Admin panel</h2>
      <Button variant="primary" onClick={() => setShowAppReg(!showAppReg)}>
        Register a new app
      </Button>
      {showAppReg && <AppRegistration></AppRegistration>}
    </div>
  );
};

export default AdminPanel;
