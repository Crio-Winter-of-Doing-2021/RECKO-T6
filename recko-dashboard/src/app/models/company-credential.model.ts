import { IReckoPartner } from './recko-partner.model';
import { ICompany } from './company.model';

export interface ICompanyCredential {
    id: number;
    name: string;
    email: string;
    password: string;
    partner: IReckoPartner;
    company: ICompany;
}