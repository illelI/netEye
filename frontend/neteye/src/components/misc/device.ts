export interface Device {
    ip: string;
    openedPorts: number[];
    hostname?: string;
    locatiom?: string;
    system?: string;
    typeOfDevice?: string;
}