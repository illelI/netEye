import './App.css';
import Login from './components/Login/Login';
import Register from './components/Register/Register'
import Home from './components/Home/Home'
import axios from 'axios';
import { useEffect } from 'react';
import React from 'react';
import { Route, Routes, BrowserRouter} from 'react-router-dom';

function App() {

  useEffect(() => {
    axios.get('http://127.0.0.1:8080/api/v1/csrf', 
    {
      withCredentials: true
    })
      .then(response => {
        document.cookie = "token="+response.data.token;
      })
      .catch(error => {
        console.error(error);
      })
  }, []);

  return (
    <React.StrictMode>
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<Home/>} />
          <Route path="/login" element={<Login/>} />
          <Route path="/register" element={<Register />} />
        </Routes>
      </BrowserRouter>
    </div>
    </React.StrictMode>
  );
}

export default App;
