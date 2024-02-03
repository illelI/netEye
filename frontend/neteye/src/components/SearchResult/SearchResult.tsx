import * as React from 'react';
import { useLocation } from "react-router-dom"
import AppNavbar from "../AppNavbar/AppNavbar";
import './SearchResult.css'
import SingleItem from '../SingleItem/SingleItem';

export const SearchResult = () => {
    const location = useLocation();
    let state = location.state;

        let content = [];
        
        content = Object.entries(state);

        const deviceArray = content.map(([key, device]) => ({
            ip: device.ip,
            openedPorts: device.openedPorts,
            hostname: device.hostname || undefined,
            location: device.location || undefined,
            system: device.system || undefined,
            typeOfDevice: device.typeOfDevice || undefined,
          }));

          const elementsArray = deviceArray.map((device) => (
            <SingleItem key={device.ip} {...device}/>
          ));


    return (
        <div className="SearchResult">
            <AppNavbar />
            {elementsArray}
        </div>
    )
}

export default SearchResult;