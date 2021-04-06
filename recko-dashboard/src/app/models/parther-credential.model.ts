import { IReckoPartner } from './recko-partner.model';

export interface IPartnerCredential {
    email: string;
    password: string;
    partner: IReckoPartner;
}