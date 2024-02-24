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
    const [currentPage, setCurrentPage] = useState(null);
    const [numberOfPages, setNumberOfPages] = useState(null);
    const [foundedDevices, setFoundedDevices] = useState(null);
    const [query, setQuery] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(endpointPrefix + '/device/find', {
                    params: {
                        criteria: criteria
                    }
                });
                setContent(response.data.devices);
                setCurrentPage(response.data.currentPage + 1);
                setNumberOfPages(response.data.numberOfPages);
                setFoundedDevices(response.data.numberOfFoundDevices);
                setQuery(response.data.query);
            } catch {
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

    let resultsFound = <p className='pFounded'>Found {foundedDevices} devices for query: {query}</p>
    if (foundedDevices == 1) {
        resultsFound = <p className='pFounded'>Found {foundedDevices} device for query: {query}</p>
    }
    
    return (
        <div className="SearchResult">
            <AppNavbar />
            <div>
                {resultsFound}
            </div>
            {elementsArray}
        </div>
    )
}

export default SearchResult;