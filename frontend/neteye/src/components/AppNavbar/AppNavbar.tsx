import * as React from 'react';
import './AppNavbar.css';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useState } from 'react';
import endpointPrefix from '../misc/constants';

export const AppNavbar = () => {

  const [searchCriteria, setSearchCriteria] = useState('');

  const navigate = useNavigate();

  const handleSubmit = ()  => {
    navigate('/search/' + searchCriteria);
  };

  return (
  <div className='AppNavbar'>
    <nav className="navbar navbar-dark bg-dark">
      <div className='flex-start'>
      <span className='navbar-name'>
        <h4 id='navbar-name-h4'>netEye</h4>
      </span>
      <form className="d-flex navbar-form" onSubmit={handleSubmit}>
        <input className="navbar-search form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search" value={searchCriteria} onChange={(e) => setSearchCriteria(e.target.value)} />
        <button className="btn btn-outline-danger my-2 my-sm-0 navbar-button" type="submit">Search</button>
      </form>
      </div>
      <div className='flex-end'>
      <button className="navbar-button btn btn-dark btn-outline-danger" onClick={() => navigate("/login")} >Sign in</button>
      <button className="navbar-button btn btn-dark btn-outline-danger" onClick={() => navigate("/register")} >Sign up</button>
      </div>
    </nav>
  </div>
  )
};

export default AppNavbar;
