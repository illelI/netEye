import './App.css';
import Login from './components/LoginAndRegister/Login';
import Register from './components/LoginAndRegister/Register'
import Home from './components/Home/Home'
import axios from 'axios';
import { useEffect } from 'react';
import React from 'react';
import { Route, Routes, BrowserRouter} from 'react-router-dom';
import endpointPrefix from './components/misc/constants';

function App() {

  useEffect(() => {
    axios.get(endpointPrefix + '/api/v1/csrf', 
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
