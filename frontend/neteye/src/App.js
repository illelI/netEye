import './App.css';
import Login from './components/LoginAndRegister/Login';
import Register from './components/LoginAndRegister/Register'
import Home from './components/Home/Home'
import DevicePage from './components/DevicePage/DevicePage'
import SearchResult from './components/SearchResult/SearchResult'
import Account from './components/Account/Account'
import AdminPanel from './components/AdminPanel/AdminPanel'
import React from 'react';
import { Route, Routes, BrowserRouter} from 'react-router-dom';
import { AuthProvider } from './components/context/AuthContext';

function App() {

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
          <Route path='/account' element={<Account />} />
          <Route path='/adminPanel' element={<AdminPanel />} />
        </Routes>
      </BrowserRouter>
    </div>
    </AuthProvider>
    </React.StrictMode>
  );
}

export default App;
