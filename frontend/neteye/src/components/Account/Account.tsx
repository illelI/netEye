import * as React from 'react';
import './Account.css';
import AppNavbar from '../AppNavbar/AppNavbar';
import * as Yup from 'yup';
import axios from 'axios';
import endpointPrefix from '../misc/constants';
import { useState } from 'react';

export const Account = () => {

    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [passwordConfirmation, setPasswordConf] = useState('');

    React.useEffect(() => {
        const fetchData = async () => {
            try {
                let response = await axios.post(endpointPrefix + "/account/getAccInfo", null, {
                    withCredentials: true
                })
                let data = response.data;
                setEmail(data.email);
                setLastName(data.lastName);
                setFirstName(data.firstName);
            } catch {

            }
            
        }
        
        fetchData();
    }, []);

    const handleChange = () => {
        axios.post(endpointPrefix + "/account/update", {
            "firstName": firstName,
            "lastName": lastName,
            "password": password,
            "passwordConfirmation": passwordConfirmation
        }, {
            withCredentials: true
        })
    }

    const handleDelete = () => {
        axios.post(endpointPrefix + '/account/delete', {}, {
            withCredentials: true
        })
    }

    return (
        <div>
            <AppNavbar/>
            <div className='accountInfo'>
                <h3>Account settings</h3>
                <form className='accForm'>
                    <label className='formLabel'>Email</label>
                    <input className='formInput' value={email} readOnly></input>
                    <label className='formLabel'>First name</label>
                    <input className='formInput' value={firstName} onChange={(e) => setFirstName(e.target.value)} ></input>
                    <label className='formLabel' >Last name</label>
                    <input className='formInput' value={lastName} onChange={(e) => setLastName(e.target.value)} ></input>
                    <label className='formLabel'>Password</label>
                    <input className='formInput' type='password' onChange={(e) => setPassword(e.target.value)} ></input>
                    <label className='formLabel'>Password confirmation</label>
                    <input className='formInput' type="password" onChange={(e) => setPasswordConf(e.target.value)} ></input>
                </form>
                <button className='btn btn-success btnAcc' onClick={handleChange}>Change</button>
                <button className='btn btn-danger btnAcc' onClick={handleDelete}>Delete account</button>
            </div>
        </div>
    )
}

export default Account;