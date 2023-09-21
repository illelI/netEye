import * as React from 'react';
import './Login.css';
import axios from 'axios';
import * as Yup from 'yup';
import { Formik, FormikHelpers } from 'formik';
import { useNavigate } from 'react-router-dom';


export const Login = () => {

  const navigate = useNavigate();

  const validationSchema = Yup.object().shape({
    email: Yup.string()
        .email("Please enter a valid email")
        .required("Email is required"),
    password: Yup.string()
        .required("Password is required")
        .min(5, "Password must have at least 5 characters")
  })

  const handleSubmit = (values : {
    email: string;
    password: string;
    }, errors: FormikHelpers<{
      email: string;
      password: string;
    }>) => {
      let token = document.cookie.split('=');
      axios.post('http://127.0.0.1:8080/account/login',{
        "email": values.email,
        "password": values.password
      }, {
        withCredentials: true,
        headers: {
          'X-Csrf-Token': token[1],
          'Content-Type':'application/json',
      }}
    ).then(response => {
        alert("It will be added later");
    }).catch(error => {
      document.getElementById("login-error").innerHTML = "Incorrect email or password";
    })
  };

  return(
    <div className="login-form-container">
      <Formik
                initialValues={{
                    email: "",
                    password: "",
                }}
                onSubmit={(values, errors) => {
                    handleSubmit(values, errors)
                }}
                validationSchema={validationSchema}
                >
                    {({values,errors,touched, handleSubmit, handleChange, handleBlur}) => {
                        return (
    <div className="login-auth-form-container">
        <h2>Login</h2>
        <form className="login-form" onSubmit={handleSubmit}>
            <label className="login-label" htmlFor="email">Email</label>
            <input className="login-input" value={values.email} onChange={handleChange("email")} onBlur={handleBlur("email")} type="text" placeholder="youremail@mail.com" id="email" name="email" />
            <div className='login-errors'>{touched.email && errors.email}</div>
            <label className="login-label" htmlFor="password">Password</label>
            <input className="login-input" value={values.password} onChange={handleChange("password")} onBlur={handleBlur("password")} type="password" placeholder="********" id="password" name="password" />
            <div className='login-errors'>{touched.password && errors.password}</div>
            <button className="login-button btn btn-dark btn-outline-danger" ><span className="login-button-text">Log In</span></button>
            <div className='login-errors' id='login-error'></div>
        </form>
        <button className="login-register-btn" onClick={() => navigate("/register")}>Don't have an account? Register here.</button>
    </div> 
  )}}
  </Formik>
  </div>
  )
};

export default Login;
