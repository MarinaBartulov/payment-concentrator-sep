import { Route, BrowserRouter as Router } from "react-router-dom";
import "./App.css";
import BitcoinCancel from "./components/BitcoinCancel";
import BitcoinError from "./components/BitcoinError";
import BitcoinSuccess from "./components/BitcoinSuccess";
import Test from "./components/Test";
import AppRegistration from "./components/AppRegistration";
import { ToastContainer } from "react-toastify";
import PayPalReturn from "./components/paypal/PayPalReturn";
import PayPalCancel from "./components/paypal/PayPalCancel";
import PayPalSuccess from "./components/paypal/PayPalSuccess";
import PayPalFail from "./components/paypal/PayPalFail";
import PayPalSubscriptionReturn from "./components/paypal/PayPalSubscriptionReturn";
import PayPalSubscriptionCancel from "./components/paypal/PayPalSubscriptionCancel";
import PayPalSubscriptionSuccess from "./components/paypal/PayPalSubscriptionSuccess";
import PayPalSubscriptionFail from "./components/paypal/PayPalSubscriptionFail";
import Subscription from "./components/Subscription";
import Header from "./components/Header";
import Home from "./components/Home";

function App() {
  return (
    <Router>
      <div className="App">
        <Header />
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
        <Route exact path="/">
          <Home />
        </Route>
        <Route exact path="/order/:orderId">
          <Test />
        </Route>
        <Route exact path="/pay/return">
          <PayPalReturn />
        </Route>
        <Route exact path="/pay/cancel/:id">
          <PayPalCancel />
        </Route>
        <Route exact path="/pay/return/success">
          <PayPalSuccess />
        </Route>
        <Route exact path="/pay/return/fail">
          <PayPalFail />
        </Route>
        <Route exact path="/subscription/id/:subscriptionId">
          <Subscription />
        </Route>
        <Route exact path="/subscription/return/:id">
          <PayPalSubscriptionReturn />
        </Route>
        <Route exact path="/subscription/cancel/:id">
          <PayPalSubscriptionCancel />
        </Route>
        <Route exact path="/subscription/success">
          <PayPalSubscriptionSuccess />
        </Route>
        <Route exact path="/subscription/fail">
          <PayPalSubscriptionFail />
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
      </div>
    </Router>
  );
}

export default App;
