import * as React from 'react';
import './AdminPanel.css';
import AppNavbar from '../AppNavbar/AppNavbar';
import axios from 'axios';
import endpointPrefix from '../misc/constants';

export const AdminPanel = () => {

    const [choosen, setChoosen] = React.useState("scanner");
    const [foundUser, setFoundUser] = React.useState(null);
    const [isFound, setIsFound] = React.useState(false);
    const [ipToDelete, setIpToDelete] = React.useState('');
    const [ipToBlackList, setIpToBlackList] = React.useState('');
    const [ipFromBlackList, setIpFromBlackList] = React.useState('');
    const [userEmail, setUserEmail] = React.useState('');
    const [startingIp, setStartingIp] = React.useState('');
    const [endingIp, setEndingIp] = React.useState('');
    const [deviceResponseTimeout, setDeviceRepsonseTimeout] = React.useState('');
    const [serviceResponseTimeout, setServiceRepsonseTimeout] = React.useState('');
    const [numberOfThreads, setNumberOfThreads] = React.useState('');
    const [http, setHttp] = React.useState("HTTP ")
    const [http8080, setHttp8080] = React.useState("HTTP8080 ")
    const [https, setHttps] = React.useState("HTTPS ")
    const [telnet, setTelnet] = React.useState("Telnet ")
    const [smtp, setSmpt] = React.useState("SMTP ")
    const [ftp, setFtp] = React.useState("FTP ")
    const [imap, setImap] = React.useState("IMAP ")
    const [pop3, setPop3] = React.useState("POP3 ")
    const [rtsp, setRtsp] = React.useState("RTSP ")

    const handleHttp = () => {
        if (http) {
            setHttp(null)
        } else {
            setHttp("HTTP ")
        }
    }

    const handleHttp8080 = () => {
        if (http8080) {
            setHttp8080(null)
        } else {
            setHttp8080("HTTP8080 ")
        }
    }

    const handleHttps = () => {
        if (https) {
            setHttps(null)
        } else {
            setHttps("HTTPS ")
        }
    }

    const handleTelnet = () => {
        if (telnet) {
            setTelnet(null)
        } else {
            setTelnet("Telnet ")
        }
    }

    const handleSmtp = () => {
        if (smtp) {
            setSmpt(null)
        } else {
            setSmpt("SMTP ")
        }
    }

    const handleFtp = () => {
        if (ftp) {
            setFtp(null)
        } else {
            setFtp("FTP ")
        }
    }

    const handleImap = () => {
        if (imap) {
            setImap(null)
        } else {
            setImap("Imap ")
        }
    }

    const handlePop3 = () => {
        if (pop3) {
            setPop3(null)
        } else {
            setPop3("POP3 ")
        }
    }

    const handleRtsp = () => {
        if (rtsp) {
            setRtsp(null)
        } else {
            setRtsp("RTSP ")
        }
    }

    const handleScaner = () => {
        setChoosen("scanner")
    }

    const handleIp = () => {
        setChoosen("ip")
    }

    const handleUser = () => {
        setChoosen("user")
    }

    const handleIpDelete = () => {
        axios.post(endpointPrefix + '/admin/delete', {
            "ip": ipToDelete
        },
        {
            withCredentials: true
        })
    }

    const handleAddIpToBlacklist = () => {
        axios.post(endpointPrefix + '/admin/addToBlacklist', {
            "ip": ipToBlackList
        },
        {
            withCredentials: true
        })
    }

    const handleDeleteIpFromBlacklist = () => {
        axios.post(endpointPrefix + '/admin/deleteFromBlacklist', {
            "ip": ipFromBlackList
        },
        {
            withCredentials: true
        })
    }

    const handleFindUser = () => {
        const fetchData = async () => {
            try {
                let response = await axios.post(endpointPrefix + '/admin/findUser', {
                    "email": userEmail
                }, {
                    withCredentials: true
                });
                let data = response.data;
                setFoundUser({
                    "email": data.email,
                    "firstName": data.firstName,
                    "lastName": data.lastName,
                    "accountType": data.accountType
                });
                setIsFound(true);
            } catch {
                setFoundUser(null)
            }
        }
        
        fetchData();
    }

    const handleChangeAcc = () => {
        axios.post(endpointPrefix + '/admin/changeAccType', {
            "email": foundUser.email
        },
        {
            withCredentials: true
        })
    }

    const handleDeleteAcc = () => {
        axios.post(endpointPrefix + '/admin/deleteAcc', {
            "email": foundUser.email
        },
        {
            withCredentials: true
        })
    }

    const handleStartScan = () => {
        let portsToScan = "";
        if(http) portsToScan+=http;
        if(http8080) portsToScan+=http8080;
        if(https) portsToScan+=https;
        if(ftp) portsToScan+=ftp;
        if(telnet) portsToScan+=telnet;
        if(smtp) portsToScan+=smtp;
        if(imap) portsToScan+=imap;
        if(pop3) portsToScan+=pop3;
        if(rtsp) portsToScan+=rtsp;
        axios.post(endpointPrefix + "/admin/scan", {
            "startingIP": startingIp,
            "endingIP": endingIp,
            "deviceResponseTimeout": deviceResponseTimeout,
            "serviceResponseTimeout": serviceResponseTimeout,
            "numberOfThreads": numberOfThreads,
            "scannedPorts": portsToScan
        }, {
            withCredentials: true
        })
    }

    let scannerContent = <div>
        <div>
            <h3 className='threeColumns choosen'>Scanner</h3>
            <h3 className='threeColumns pointer' onClick={handleIp}>IP deletion & blacklist</h3>
            <h3 className='threeColumns pointer' onClick={handleUser}>Managing users</h3>
        </div>
        <form className='scannerForm'>
            <div className='scanningProps'>
            <label className='adminLabel' >Starting ip</label>
            <input className='adminInput' onChange={(e) => setStartingIp(e.target.value)}></input>
            <label className='adminLabel'>Ending ip</label>
            <input className='adminInput' onChange={(e) => setEndingIp(e.target.value)}></input>
            <label className='adminLabel'>Number of threads</label>
            <input className='adminInput' onChange={(e) => setNumberOfThreads(e.target.value)}></input>
            <label className='adminLabel'>Device response timeout</label>
            <input className='adminInput' onChange={(e) => setDeviceRepsonseTimeout(e.target.value)}></input>
            <label className='adminLabel'>Service response timeout</label>
            <input className='adminInput' onChange={(e) => setServiceRepsonseTimeout(e.target.value)}></input>
            </div>
            <h5>Scanned ports:</h5>
            <div className='scannedPorts'>
            <label className='adminLabel scannerCheckbox'>FTP (21)</label>
            <input className='adminCheck' type='checkbox' name="FTP" value="FTP " defaultChecked onChange={handleFtp}></input>
            <label className='adminLabel scannerCheckbox'>Telnet (23)</label>
            <input className='adminCheck' type='checkbox' name="Telnet" value="Telnet " defaultChecked onChange={handleTelnet}></input>
            <label className='adminLabel scannerCheckbox'>SMTP (25)</label>
            <input className='adminCheck' type='checkbox' name="SMTP" value="SMTP " defaultChecked onChange={handleSmtp}></input>
            <label className='adminLabel scannerCheckbox'>HTTP (80)</label>
            <input className='adminCheck' type='checkbox' name="HTTP" value="HTTP " defaultChecked onChange={handleHttp}></input>
            <label className='adminLabel scannerCheckbox'>HTTP (8080)</label>
            <input className='adminCheck' type='checkbox' name="HTTP8080" value="HTTP8080 " defaultChecked onChange={handleHttp8080}></input>
            <label className='adminLabel scannerCheckbox'>HTTPS (443)</label>
            <input className='adminCheck' type='checkbox' name="HTTPS" value="HTTPS " defaultChecked onChange={handleHttps}></input>
            <label className='adminLabel scannerCheckbox'>POP3 (110)</label>
            <input className='adminCheck' type='checkbox' name = "POP3" value="POP3 " defaultChecked onChange={handlePop3}></input>
            <label className='adminLabel scannerCheckbox'>IMAP (143)</label>
            <input className='adminCheck' type='checkbox' name="IMAP" value="IMAP " defaultChecked onChange={handleImap}></input>
            <label className='adminLabel scannerCheckbox'>RTSP (554)</label>
            <input className='adminCheck' type='checkbox' name="RTSP" value="RTSP " defaultChecked onChange={handleRtsp}></input>
            </div>
            <button type='button' className='btn btn-success scanbtn' onClick={handleStartScan}>Scan</button>
        </form>
    </div>

    let ipContent = <div>
        <div>
            <h3 className='threeColumns pointer' onClick={handleScaner}>Scanner</h3>
            <h3 className='threeColumns choosen'>IP deletion & blacklist</h3>
            <h3 className='threeColumns pointer' onClick={handleUser}>Managing users</h3>
        </div>
        <div className='ipDeletion'>
            <h5>Ip deletion</h5>
            <form className='ipForm'>
                <label className='adminLabel'>IP to delete from database</label>
                <input className='adminInput' onChange={(e) => setIpToDelete(e.target.value)}></input>
                <button type="button" className='btn btn-danger ipbtn' onClick={handleIpDelete}>Delete</button>
            </form>
        </div>
        <div className='ipBlacklist'>
            <h5>Ip blacklist</h5>
            <form className='ipForm'>
                <label className='adminLabel'>IP to add to blacklist</label>
                <input className='adminInput' onChange={(e) => setIpToBlackList(e.target.value)}></input>
                <button type='button' className='btn btn-danger ipbtn' onClick={handleAddIpToBlacklist}>Add</button>
            </form>
            <form className='ipForm'>
                <label className='adminLabel'>IP to delete from blacklist</label>
                <input className='adminInput' onChange={(e) => setIpFromBlackList(e.target.value)}></input>
                <button type='button' className='btn btn-success ipbtn' onClick={handleDeleteIpFromBlacklist}>Delete</button>
            </form>
        </div>
    </div>

    let foundDiv = <div>
        {foundUser ? <div><h5>Found user</h5>
            <h6>Email: {foundUser.email}</h6>
            <h6>First name: {foundUser.firstName}</h6>
            <h6>Last name: {foundUser.lastName}</h6>
            <h6>Account type: {foundUser.accountType}</h6>
            <button type='button' className='btn btn-danger userbtn' onClick={handleDeleteAcc}>Delete</button>
            <button type='button' className='btn btn-secondary userbtn' onClick={handleChangeAcc}>Chagne account type</button> </div> :
            <></> }
        
    </div>

    let notFoundDiv = <div>
        <h5>User not found</h5>
    </div>

    let userContent = <div>
        <div>
            <h3 className='threeColumns pointer' onClick={handleScaner}>Scanner</h3>
            <h3 className='threeColumns pointer' onClick={handleIp}>IP deletion & blacklist</h3>
            <h3 className='threeColumns choosen'>Managing users</h3>
        </div>
        <form className='twoColumns ipForm'>
                <label className='adminLabel'>Search user</label>
                <input className='adminInput' onChange={(e) => setUserEmail(e.target.value)}></input>
                <button type='button' className='btn btn-success usrbtn' onClick={handleFindUser}>Search</button>
        </form>
        <div className='twoColumns foundUser'>
            {foundUser != null ? foundDiv : notFoundDiv}
        </div>
    </div>
    

    return (
        <div>
            <AppNavbar/>
            <div className="adminPanelDiv">
            {choosen === "scanner" ? scannerContent : (choosen === "ip" ?
            ipContent : userContent)}
            </div>
        </div>
    )
}

export default AdminPanel;