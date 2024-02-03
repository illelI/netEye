import * as React from 'react';
import './SingleItem.css';
import { Device } from '../misc/device';


const SingleItem = (data : Device) => {

  let hostname: React.ReactElement = <></>;
  let location: React.ReactElement = <></>;
  let system: React.ReactElement = <></>;
  let type: React.ReactElement = <span>Type: {data.typeOfDevice}</span>;
  let openedPorts = [];

  if (data.hostname != data.ip) {
    hostname = <span>{data.hostname}</span>;
  }

  if (data.location != "") {
  }

  if (data.system != "" && data.system != undefined) {
    system = <span>OS: {data.system}</span>
  }
  
  for (let port of data.openedPorts) {
    openedPorts.push(
      <div className='portBox'>{port}</div>
    )
  }

  return (
  <div className="SingleItem">
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
