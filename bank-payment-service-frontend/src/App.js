import './App.css';
import Button from "react-bootstrap/Button";
import { testService } from "./services/test-service";

function App() {

  function pay() {
    testService.test();
  }

  return (
    <div className="App">
      <h1>UniCredit Bank</h1>
      <header className="App-header">
        <p>
          <Button
            variant="dark"
            onClick={() => {
              pay();
            }}
          >
            {" "}
            Pay{" "}
          </Button>{" "}
        </p>
      </header>
    </div>
  );
}

export default App;
