import * as React from 'react';
import {FC} from 'react';
import './Register.css';
import { Formik, FormikHelpers } from 'formik';
import * as Yup from 'yup';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';


export const Register = () => {

  const navigate = useNavigate();

  const validationSchema = Yup.object().shape({
    email: Yup.string()
        .email("Please enter a valid email")
        .required("Email is required"),
    firstName: Yup.string()
        .required("First name is required")
        .min(2, "First name must have at least 2 characters"),
    lastName: Yup.string()
        .required("Last name is required")
        .min(2, "Last name must have at least 2 characters"),
    password: Yup.string()
        .required("Password is required")
        .min(5, "Password must have at least 5 characters"),
    passwordConfirmation: Yup.string()
        .required("Passwords must match")
        .oneOf([Yup.ref('password'), null], "Passwords must match")
  })

  const handleSubmit = (values : {
    email: string;
    firstName: string;
    lastName: string;
    password: string;
    passwordConfirmation: string;
    }, errors: FormikHelpers<{
      email: string;
      firstName: string;
      lastName: string;
      password: string;
      passwordConfirmation: string;
    }>) => {
      console.log("XD")
      let token = document.cookie.split('=');
      axios.post('http://127.0.0.1:8080/account/register',{
        "email": values.email,
        "firstName": values.firstName,
        "lastName": values.lastName,
        "password": values.password,
        "passwordConfirmation": values.passwordConfirmation
      }, {
        withCredentials: true,
        headers: {
          'X-Csrf-Token': token[1],
          'Content-Type':'application/json',
      }}
    ).then(response => {
        alert("It will be added later");
    }).catch(error => {
      document.getElementById("register-error").innerHTML = "User with this email already exist";
    })
  };

  return(
    <div className="register-form-container">
      <Formik
                initialValues={{
                    email: "",
                    firstName: "",
                    lastName: "",
                    password: "",
                    passwordConfirmation: ""
                }}
                onSubmit={(values, errors) => {
                    handleSubmit(values, errors)
                }}
                validationSchema={validationSchema}
                >
                    {({values,errors,touched, handleSubmit, handleChange, handleBlur}) => {
                        return (
    <div className="register-auth-form-container">
        <h2>Register</h2>
        <form className="register-form" onSubmit={handleSubmit}>
            <label className="register-label" htmlFor="email">Email</label>
            <input className="register-input" value={values.email} onChange={handleChange("email")} onBlur={handleBlur("email")} type="text" placeholder="youremail@mail.com" id="email" name="email" />
            <div className='register-errors'>{touched.email && errors.email}</div>
            <label className="register-label" htmlFor="firstName">First name</label>
            <input className="register-input" value={values.firstName} onChange={handleChange("firstName")} onBlur={handleBlur("firstName")} type="text" placeholder="First name" id="firstName" name="firstName" />
            <div className='register-errors'>{touched.firstName && errors.firstName}</div>
            <label className="register-label" htmlFor="lastName">Last name</label>
            <input className="register-input" value={values.lastName} onChange={handleChange("lastName")} onBlur={handleBlur("lasttName")} type="text" placeholder="Last name" id="lastName" name="lastName" />
            <div className='register-errors'>{touched.firstName && errors.firstName}</div>
            <label className="register-label" htmlFor="password">Password</label>
            <input className="register-input" value={values.password} onChange={handleChange("password")} onBlur={handleBlur("password")} type="password" placeholder="********" id="password" name="password" />
            <div className='register-errors'>{touched.password && errors.password}</div>
            <label className="register-label" htmlFor="passwordConfirmation">Password confirmation</label>
            <input className="register-input" value={values.passwordConfirmation} onChange={handleChange("passwordConfirmation")} onBlur={handleBlur("passwordConfirmation")} type="password" placeholder="********" id="passwordConfirmation" name="passwordConfirmation" />
            <div className='register-errors'>{touched.passwordConfirmation && errors.passwordConfirmation}</div>
            <button className="register-button btn btn-dark btn-outline-danger" type='submit'><span className="register-button-text">Register</span></button>
            <div className='register-errors' id='register-error'></div>
        </form>
        <button className="register-login-btn" type="button" onClick={() => navigate("/login")}>Already have an account? Log in here.</button>
    </div> 
  )}}
  </Formik>
  </div>
  )
};

export default Register;