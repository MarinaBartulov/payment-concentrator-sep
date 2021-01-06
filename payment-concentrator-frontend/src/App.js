import { Route, BrowserRouter as Router } from "react-router-dom";
import "./App.css";
import BitcoinCancel from "./components/BitcoinCancel";
import BitcoinError from "./components/BitcoinError";
import BitcoinSuccess from "./components/BitcoinSuccess";
import Test from "./components/Test";
import PayPalReturn from "./components/PayPalReturn";
import PayPalCancel from "./components/PayPalCancel";
import PayPalSuccess from "./components/PayPalSuccess";
import PayPalFail from "./components/PayPalFail";
import AppRegistration from "./components/AppRegistration";
import { ToastContainer } from "react-toastify";

function App() {
  return (
    <div className="App">
      <ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar={true}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
      />
      <Router>
        <Route exact path="/">
          <Test />
        </Route>
        <Route exact path="/order/:orderId">
          <Test />
        </Route>
        <Route exact path="/pay/return">
          <PayPalReturn />
        </Route>
        <Route path="/pay/cancel/:id">
          <PayPalCancel />
        </Route>
        <Route path="/pay/return/success">
          <PayPalSuccess />
        </Route>
        <Route path="/pay/return/fail">
          <PayPalFail />
        </Route>
        <Route exact path="/bitcoin/success/:id">
          <BitcoinSuccess />
        </Route>
        <Route exact path="/bitcoin/cancel/:id">
          <BitcoinCancel />
        </Route>
        <Route exact path="/bitcoin/error">
          <BitcoinError />
        </Route>
        <Route exact path="/appRegistration">
          <AppRegistration />
        </Route>
      </Router>
    </div>
  );
}

export default App;
