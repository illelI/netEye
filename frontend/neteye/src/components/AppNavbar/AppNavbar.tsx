import * as React from 'react';
import './AppNavbar.css';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useState } from 'react';
import endpointPrefix from '../misc/constants';
import { useAuth } from '../context/AuthContext';
import 'bootstrap/dist/css/bootstrap.css';
export const AppNavbar = () => {

  const [searchCriteria, setSearchCriteria] = useState('');
  const {user, logout} = useAuth();

  const [isExpanded, setIsExpanded] = useState(false);

  const handleToggle = () => {
    setIsExpanded(!isExpanded);
  };

  const navigate = useNavigate();
  
  let userDiv: React.ReactElement = <></>

  const nameClicked = () => {
    navigate('/');
  }

  const handleSubmit = ()  => {
    navigate('/search/' + searchCriteria);
  };

const handleLogout = () => {
  axios.post(endpointPrefix + '/account/logout', {}, {
    withCredentials: true
  });
  logout();
  navigate('/');
}

const handleAccount = () => {
  navigate('/account');
}

const handleAdmin = () => {
  navigate('/adminPanel');
}

  

if (user != null && user.email) {
  let acc = user.acc;
  userDiv = <div className="dropdown userDiv">
  <button className="btn btn-secondary dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" onClick={handleToggle}>
  {user.email}
  </button>
  {isExpanded && (
        <div className='dropdown-menu userMenu'>
        <a className='dropdown-item' onClick={handleAccount}>My account</a>
        {acc === "ADMIN" ? <a className='dropdown-item' onClick={handleAdmin}>Admin panel</a> : <></>}
        <a className='dropdown-item' onClick={handleLogout}>Logout</a>
      </div> 
      )}
</div>
} 

  return (
  <div className='AppNavbar'>
    <nav className="navbar navbar-dark bg-dark">
      <div className='flex-start'>
      <span className='navbar-name' onClick={nameClicked}>
        <h4 id='navbar-name-h4'>netEye</h4>
      </span>
      <form className="d-flex navbar-form" onSubmit={handleSubmit}>
        <input className="navbar-search form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search" value={searchCriteria} onChange={(e) => setSearchCriteria(e.target.value)} />
        <button className="btn search-button my-2 my-sm-0 navbar-button" type="submit">Search</button>
      </form>
      </div>
      <div className='flex-end endDiv'>
      {user ? userDiv :
      <div><button className="navbar-button btn btn-dark btn-outline-success" onClick={() => navigate("/login")} >Sign in</button>
      <button className="navbar-button btn btn-dark btn-outline-danger" onClick={() => navigate("/register")} >Sign up</button></div> }
      </div>
    </nav>
  </div>
  )
};

export default AppNavbar;
