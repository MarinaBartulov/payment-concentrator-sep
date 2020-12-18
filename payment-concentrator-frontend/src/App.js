import { Route, BrowserRouter as Router } from "react-router-dom";
import "./App.css";
import BitcoinCancel from "./components/BitcoinCancel";
import BitcoinError from "./components/BitcoinError";
import BitcoinSuccess from "./components/BitcoinSuccess";
import BitcoinTestPayment from "./components/BitcoinTestPayment";
import Test from "./components/Test";

function App() {
  return (
    <div className="App">
      <h1>Payment Concentrator</h1>
      <Router>
        <Route exact path="/bitcoinPayment">
          <BitcoinTestPayment />
        </Route>
        <Route exact path="/bitcoinSuccess/:id">
          <BitcoinSuccess />
        </Route>
        <Route exact path="/bitcoinCancel/:id">
          <BitcoinCancel />
        </Route>
        <Route exact path="/bitcoinError">
          <BitcoinError />
        </Route>
        {/* <Route exact path="/">
          <Test />
        </Route>
        <Route exact path="/:orderId">
          <Test />
        </Route> */}
      </Router>
    </div>
  );
}

export default App;
