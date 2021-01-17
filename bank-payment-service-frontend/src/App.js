import './App.css';
import Home from './components/Home';
import ClientForm from './components/ClientForm';
import MerchantForm from './components/MerchantForm';
import { Route, BrowserRouter as Router } from "react-router-dom";
import { ToastContainer } from "react-toastify";

function App() {

  return (
    <div className="App">
      <Router>
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
