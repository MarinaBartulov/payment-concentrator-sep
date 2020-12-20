import './App.css';
import Home from './components/Home';
import ClientForm from './components/ClientForm';
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
      </Router>
    </div>
  );
}

export default App;
