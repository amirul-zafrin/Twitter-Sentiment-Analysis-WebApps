import './App.css';
import Home from './pages/Home'
import Search from './pages/Results';
import Test from './pages/Test';
import { BrowserRouter, Routes, Route } from "react-router-dom"

function App() {
  return <BrowserRouter>
    <Routes>
      <Route path='/' element={<Home />} />
      <Route path='/results' element={<Search />}></Route>
      <Route path='/testing' element={<Test/>}></Route>
  </Routes>
  </BrowserRouter>
}

export default App;
