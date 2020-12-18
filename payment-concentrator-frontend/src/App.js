import { Route, BrowserRouter as Router } from "react-router-dom";
import "./App.css";
import Test from "./components/Test";
import PayPalReturn from "./components/PayPalReturn";
import PayPalCancel from "./components/PayPalCancel";
import PayPalSuccess from "./components/PayPalSuccess";
import PayPalFail from "./components/PayPalFail";

function App() {
  return (
    <div className="App">
      <Routes>
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
      </Routes>
    </div>
  );
}

export default App;
