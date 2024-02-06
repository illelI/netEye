import './DevicePage.css'
import axios from 'axios';
import * as React from 'react';
import { useLocation, useParams } from "react-router-dom";
import endpointPrefix from '../misc/constants';
import { useEffect, useState } from 'react';
import AppNavbar from '../AppNavbar/AppNavbar';

export const DevicePage = () => {

    const { ip } = useParams();
    const [deviceInfo, setDeviceInfo] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(endpointPrefix + '/device/getDeviceInfo', {
                    params: {
                        ip: ip
                    }
                });
                setDeviceInfo(response.data);
            } catch {
            } finally {
                setLoading(false);
            }
        }

        fetchData();
    }, [ip] );
    
    if(loading) {
        return (
            <p>loading</p>
        )
    }

    if (deviceInfo == null) {
        return (
            <p>Not found</p>
        )
    }

    return (
        <div className='DevicePage'>
            <AppNavbar />
            <div className='deviceColumn'>
                <h3>{deviceInfo.ip}</h3>
            </div>
            <div className='deviceColumn'>kupa</div>
        </div>
    )
}

export default DevicePage;