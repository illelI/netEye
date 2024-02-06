import * as React from 'react';
import './SingleItem.css';
import { Device } from '../misc/device';
import { useNavigate } from 'react-router-dom';


const SingleItem = (data : Device) => {

  const navigate = useNavigate();

  let hostname: React.ReactElement = <></>;
  let location: React.ReactElement = <></>;
  let system: React.ReactElement = <></>;
  let type: React.ReactElement = <span>Type: {data.typeOfDevice}</span>;
  let openedPorts = [];

  if (data.hostname != data.ip) {
    hostname = <span>{data.hostname}</span>;
  }

  if (data.location != "") {
    location = <span>Location: {data.location}</span>
  }

  if (data.system != "" && data.system != undefined) {
    system = <span>OS: {data.system}</span>
  }
  
  for (let openedPort of data.portInfo) {
    openedPorts.push(
      <div className='portBox'>{openedPort.port}</div>
    )
  }

  const handleClick = () => {
    navigate("/device/" + data.ip)
  }

  return (
  <div className="SingleItem" onClick={handleClick}>
    <h4 className='ip'>{data.ip}</h4>
    <div className='column'>
    <ul className='infoList'>
      <li>{hostname}</li>
      <li>{location}</li>
      <li>{system}</li>
      <li>{type}</li>
    </ul>
    </div>
    <div className='column'>
      <div>Opened ports:</div>
      {openedPorts}
    </div>
  </div>
)};

export default SingleItem;
