import { Route, BrowserRouter as Router } from "react-router-dom";
import "./App.css";
import BitcoinCancel from "./components/BitcoinCancel";
import BitcoinError from "./components/BitcoinError";
import BitcoinSuccess from "./components/BitcoinSuccess";
import Payment from "./components/Payment";
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
import Home from "./components/Home";
import Login from "./components/Login";
import AdminPanel from "./components/AdminPanel";
import MerchantChoosePM from "./components/MerchantChoosePM";
import MerchantChangePM from "./components/MerchantChangePM";
import BillingPlan from "./components/BillingPlan";

function App() {
  return (
    <Router>
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
        <Route exact path="/">
          <Home />
        </Route>
        <Route exact path="/order/:orderId">
          <Payment />
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
        <Route exact path="/login">
          <Login />
        </Route>
        <Route exact path="/adminPanel">
          <AdminPanel />
        </Route>
        <Route exact path="/merchantChoosePM">
          <MerchantChoosePM />
        </Route>
        <Route exact path="/merchantChangePM">
          <MerchantChangePM />
        </Route>
        <Route exact path="/billingPlan">
          <BillingPlan />
        </Route>
      </div>
    </Router>
  );
}

export default App;
