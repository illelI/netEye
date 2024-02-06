import { Port } from "./port";

export interface Device {
    ip: string;
    portInfo: Port[];
    hostname?: string;
    location?: string;
    system?: string;
    typeOfDevice?: string;
}