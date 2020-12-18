import { Route, BrowserRouter as Router } from "react-router-dom";
import "./App.css";
import Test from "./components/Test";

function App() {
  return (
    <div className="App">
      <h1>Payment Concentrator</h1>
        <Router>
          <Route exact path="/:orderId">
            <Test />
          </Route>
        </Router>
    </div>
  );
}

export default App;
