import * as React from 'react';
import './AdminPanel.css';
import AppNavbar from '../AppNavbar/AppNavbar';

export const AdminPanel = () => {

    const [choosen, setChoosen] = React.useState("scanner");
    const [foundUser, setFoundUser] = React.useState(null);

    const handleScaner = () => {
        setChoosen("scanner")
    }

    const handleIp = () => {
        setChoosen("ip")
    }

    const handleUser = () => {
        setChoosen("user")
    }

    let scannerContent = <div>
        <div>
            <h3 className='threeColumns choosen'>Scanner</h3>
            <h3 className='threeColumns pointer' onClick={handleIp}>IP deletion & blacklist</h3>
            <h3 className='threeColumns pointer' onClick={handleUser}>Managing users</h3>
        </div>
        <form className='scannerForm'>
            <div className='scanningProps'>
            <label className='adminLabel'>Starting ip</label>
            <input className='adminInput'></input>
            <label className='adminLabel'>Ending ip</label>
            <input className='adminInput'></input>
            <label className='adminLabel'>Number of threads</label>
            <input className='adminInput'></input>
            <label className='adminLabel'>Device response timeout</label>
            <input className='adminInput'></input>
            <label className='adminLabel'>Service response timeout</label>
            <input className='adminInput'></input>
            </div>
            <h5>Scanned ports:</h5>
            <div className='scannedPorts'>
            <label className='adminLabel scannerCheckbox'>FTP (21)</label>
            <input className='adminCheck' type='checkbox' name="FTP" value="FTP " defaultChecked></input>
            <label className='adminLabel scannerCheckbox'>Telnet (23)</label>
            <input className='adminCheck' type='checkbox' name="Telnet" value="Telnet " defaultChecked></input>
            <label className='adminLabel scannerCheckbox'>SMTP (25)</label>
            <input className='adminCheck' type='checkbox' name="SMTP" value="SMTP " defaultChecked></input>
            <label className='adminLabel scannerCheckbox'>HTTP (80)</label>
            <input className='adminCheck' type='checkbox' name="HTTP" value="HTTP " defaultChecked></input>
            <label className='adminLabel scannerCheckbox'>HTTP (8080)</label>
            <input className='adminCheck' type='checkbox' name="HTTP8080" value="HTTP8080 " defaultChecked></input>
            <label className='adminLabel scannerCheckbox'>HTTPS (443)</label>
            <input className='adminCheck' type='checkbox' name="HTTPS" value="HTTPS " defaultChecked></input>
            <label className='adminLabel scannerCheckbox'>POP3 (110)</label>
            <input className='adminCheck' type='checkbox' name = "POP3" value="POP3 " defaultChecked></input>
            <label className='adminLabel scannerCheckbox'>IMAP (143)</label>
            <input className='adminCheck' type='checkbox' name="IMAP" value="IMAP " defaultChecked></input>
            <label className='adminLabel scannerCheckbox'>RTSP (554)</label>
            <input className='adminCheck' type='checkbox' name="RTSP" value="RTSP " defaultChecked></input>
            </div>
            <button className='btn btn-success scanbtn' type='submit'>Scan</button>
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
                <input className='adminInput'></input>
                <button type='submit' className='btn btn-danger ipbtn'>Delete</button>
            </form>
        </div>
        <div className='ipBlacklist'>
            <h5>Ip blacklist</h5>
            <form className='ipForm'>
                <label className='adminLabel'>IP to add to blacklist</label>
                <input className='adminInput'></input>
                <button type='submit' className='btn btn-danger ipbtn'>Add</button>
            </form>
            <form className='ipForm'>
                <label className='adminLabel'>IP to delete from blacklist</label>
                <input className='adminInput'></input>
                <button type='submit' className='btn btn-success ipbtn'>Delete</button>
            </form>
        </div>
    </div>

    let userContent = <div>
        <div>
            <h3 className='threeColumns pointer' onClick={handleScaner}>Scanner</h3>
            <h3 className='threeColumns pointer' onClick={handleIp}>IP deletion & blacklist</h3>
            <h3 className='threeColumns choosen'>Managing users</h3>
        </div>
        <form className='twoColumns ipForm'>
                <label className='adminLabel'>Search user</label>
                <input className='adminInput'></input>
                <button type='submit' className='btn btn-success usrbtn'>Search</button>
        </form>
        <div className='twoColumns foundUser'>
            <h5>Found user</h5>
            <h6>Email: admin@neteye.com</h6>
            <h6>First name: admin</h6>
            <h6>Last name: admin</h6>
            <h6>Account type: ADMIN</h6>
            <button className='btn btn-danger userbtn'>Delete</button>
            <button className='btn btn-secondary userbtn'>Chagne account type</button>
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