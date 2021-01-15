import './App.css';
import Home from './components/Home';
import ClientForm from './components/ClientForm';
import MerchantForm from './components/MerchantForm';
import { Route, BrowserRouter as Router } from "react-router-dom";

function App() {

  return (
    <div className="App">
      <Router>
        <Route exact path="/">
          <Home />
        </Route>
        <Route path="/issuer/:paymentId">
          <ClientForm />
        </Route>
        <Route path="/card-info/:metchantId">
          <MerchantForm />
        </Route>
      </Router>
    </div>
  );
}

export default App;
