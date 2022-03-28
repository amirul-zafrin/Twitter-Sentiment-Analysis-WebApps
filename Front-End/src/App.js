import './App.css';
import Home from './pages/Home'
import Search from './pages/Results';
import { BrowserRouter, Routes, Route } from "react-router-dom"

function App() {
  return <BrowserRouter>
    <Routes>
      <Route path='/' element={<Home />} />
      <Route path='/results' element={<Search />}></Route>
  </Routes>
  </BrowserRouter>
}

export default App;
