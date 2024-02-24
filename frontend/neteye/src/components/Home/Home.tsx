import * as React from 'react';
import './Home.css';
import AppNavbar from '../AppNavbar/AppNavbar';


export const Home = () => (
  <div className="Home">
    <AppNavbar></AppNavbar>
    <div className='homeInfo'>
      <h3>How to search?</h3>
      <p>
        <label>By default neteye search devices by port info, so search query </label> <br/>
        <span className='queryExample'>html</span> <br/>
        <label>will list devices that contains word "html" in their banners. If you want to perform more complex searches you should use filters. For example
          if you want to search for devices that contains word "html" in their banners, runs on port 443 and uses Nginx as server your query will look like</label> <br/>
        <span className='queryExample'>html port:443 appName:nginx</span> <br/>
      </p>
      <h3>List of filters</h3>
      <table>
        <thead>
          <tr>
            <td className='filterColumn'><strong>Filter</strong></td>
            <td><strong>Description</strong></td>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>ip</td>
            <td>Device ip</td>
          </tr>
          <tr>
            <td>ipRange</td>
            <td>Devices with ip in given range</td>
          </tr>
          <tr>
            <td>port</td>
            <td>Opened port</td>
          </tr>
          <tr>
            <td>location</td>
            <td>Device location; It could be city, country or part of their name</td>
          </tr>
          <tr>
            <td>system</td>
            <td>Operating system used by device</td>
          </tr>
          <tr>
            <td>type</td>
            <td>Type of device, either server or camera. Note: the type of device is determined by its behavior on port 554. Because of that some results may be inccorect.</td>
          </tr>
          <tr>
            <td>appName</td>
            <td>Name of the app running on given port</td>
          </tr>
          <tr>
            <td>appVersion</td>
            <td>Version of the app running on given port</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
);

export default Home;
