import * as React from 'react';
import './Account.css';
import AppNavbar from '../AppNavbar/AppNavbar';
import * as Yup from 'yup';
import axios from 'axios';
import endpointPrefix from '../misc/constants';

export const Account = () => {

    React.useEffect(() => {
        const fetchData = async () => {
            try {
                let response = await axios.post(endpointPrefix + "/account/getAccInfo", {
                    withCredentials: true
                })
                let data = response.data;
            } catch {

            }
            
        }
        
        fetchData();
    });
    
    const validationSchema = Yup.object().shape({
        email: Yup.string()
            .email("Please enter a valid email"),
        password: Yup.string()
            .min(5, "Password must have at least 5 characters")
      })

    return (
        <div>
            <AppNavbar/>
            <div className='accountInfo'>
                <h3>Account settings</h3>
                <form className='accForm'>
                    <label className='formLabel'>Email</label>
                    <input className='formInput'></input>
                    <label className='formLabel'>First name</label>
                    <input className='formInput'></input>
                    <label className='formLabel'>Last name</label>
                    <input className='formInput'></input>
                    <label className='formLabel'>Password</label>
                    <input className='formInput'></input>
                    <label className='formLabel'>Password confirmation</label>
                    <input className='formInput'></input>
                </form>
                <button className='btn btn-success btnAcc'>Change</button>
                <button className='btn btn-danger btnAcc'>Delete account</button>
            </div>
        </div>
    )
}

export default Account;