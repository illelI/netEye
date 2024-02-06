import * as React from 'react';
import { useLocation, useParams } from "react-router-dom"
import AppNavbar from "../AppNavbar/AppNavbar";
import './SearchResult.css'
import SingleItem from '../SingleItem/SingleItem';
import { useEffect, useState } from 'react';
import axios from 'axios';
import endpointPrefix from '../misc/constants';
import { Device } from '../misc/device';

export const SearchResult = () => {
    const { criteria } = useParams();
    const [content, setContent] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(endpointPrefix + '/device/find', {
                    params: {
                        criteria: criteria
                    }
                });
                setContent(response.data);
            } catch {
                console.log("dupa")
            } finally {
                setLoading(false);
            }
        }

        fetchData();

    }, [criteria]);

    if(loading) {
        return (
            <div>Loading</div>
        )
    } else if(content == null) {
        return (
            <div>error</div>
        )
    }

    let contentArray = Object.entries(content);
        

    const deviceArray = contentArray.map(([key, device]: [string, Device]) => ({
        ip: device.ip,
        portInfo: device.portInfo,
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