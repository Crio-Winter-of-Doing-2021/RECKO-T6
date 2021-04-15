import { IReckoPartner } from './recko-partner.model';
import { ICompany } from './company.model';

export interface ICompanyCredential {
    email: string;
    password: string;
    partner: IReckoPartner;
    company: ICompany;
}