import { Route, BrowserRouter as Router } from "react-router-dom";
import "./App.css";
import BitcoinCancel from "./components/BitcoinCancel";
import BitcoinError from "./components/BitcoinError";
import BitcoinSuccess from "./components/BitcoinSuccess";
import BitcoinTestPayment from "./components/BitcoinTestPayment";
import Test from "./components/Test";
import PayPalReturn from "./components/PayPalReturn";
import PayPalCancel from "./components/PayPalCancel";
import PayPalSuccess from "./components/PayPalSuccess";
import PayPalFail from "./components/PayPalFail";

function App() {
  return (
    <div className="App">
      <h1>Payment Concentrator</h1>
      <Router>
        <Route exact path="/">
          <Test />
        </Route>
        <Route exact path="/:orderId">
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
      </Router>
    </div>
  );
}

export default App;
