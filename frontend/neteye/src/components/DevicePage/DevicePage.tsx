import './DevicePage.css'
import axios from 'axios';
import * as React from 'react';
import { useParams } from "react-router-dom";
import endpointPrefix from '../misc/constants';
import { useEffect, useState } from 'react';
import AppNavbar from '../AppNavbar/AppNavbar';
import { Device } from '../misc/device';
import { Port } from '../misc/port';

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

    let device: Device = deviceInfo;

    let hostname: React.ReactElement = <></>
    let location: React.ReactElement = <></>
    let system: React.ReactElement = <></>
    let typeOfDevice: React.ReactElement = <tr><td>Type</td><td>{device.typeOfDevice}</td></tr>

    if(device.hostname != device.ip) {
        hostname = <tr><td>Hostname</td><td>{device.hostname}</td></tr>
    }

    if(device.location != '' && device.location != null && device.location != undefined) {
        location = <tr><td>Location</td><td>{device.location}</td></tr>
    }

    if(device.system != '' && device.system != null && device.system != undefined) {
        system = <tr><td>System</td><td>{device.system}</td></tr>
    }

    let portElements: React.ReactElement[] = [];
    let portInfoElements: React.ReactElement[] = [];

    for (let port of device.portInfo) {
        let appName = "";
        let appVersion = "";
        console.log(port.info)

        if (port.appName != null) {
            appName = port.appName;
        }

        if (port.appVersion != null) {
            appVersion = port.appVersion;
        }

        portElements.push(
            <a href={`#${port.port}`}>{port.port}</a>
        );

        portInfoElements.push(
            <div id={`${port.port}`} className='portInfo'>
                <hr/>
                <h5>{port.port}</h5>
                <h6>{appName + " " + appVersion}</h6>
                <p>{port.info}</p>
            </div>
        );

    }

    return (
        <div className='DevicePage'>
            <AppNavbar />
            <div className='deviceColumn'>
                <h3>{deviceInfo.ip}</h3>
                <table className='deviceInfoTable'>
                    <tbody>
                        {hostname}
                        {location}
                        {system}
                        {typeOfDevice}
                    </tbody>
                </table>
            </div>
            <div className='deviceColumn'>
                <h5>Opened ports</h5>
                <div className='openedPorts'>
                    {portElements}
                </div>
                {portInfoElements}
            </div>
        </div>
    )
}

export default DevicePage;