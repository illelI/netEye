export interface Device {
    ip: string;
    openedPorts: number[];
    hostname?: string;
    location?: string;
    system?: string;
    typeOfDevice?: string;
}