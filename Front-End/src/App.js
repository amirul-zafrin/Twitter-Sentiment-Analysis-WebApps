import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./components/Hero";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" exact element={<Home />}></Route>
        {/* <Route path="/search" exact element={}></Route> */}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
