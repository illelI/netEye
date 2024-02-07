import './App.css';
import Login from './components/LoginAndRegister/Login';
import Register from './components/LoginAndRegister/Register'
import Home from './components/Home/Home'
import DevicePage from './components/DevicePage/DevicePage'
import SearchResult from './components/SearchResult/SearchResult'
import axios from 'axios';
import { useEffect } from 'react';
import React from 'react';
import { Route, Routes, BrowserRouter} from 'react-router-dom';
import endpointPrefix from './components/misc/constants';
import { AuthProvider } from './components/context/AuthContext';

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
      <AuthProvider>
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<Home/>} />
          <Route path="/login" element={<Login/>} />
          <Route path="/register" element={<Register />} />
          <Route path='/search/:criteria' element={<SearchResult />} />
          <Route path='/device/:ip' element={<DevicePage />} />
        </Routes>
      </BrowserRouter>
    </div>
    </AuthProvider>
    </React.StrictMode>
  );
}

export default App;
