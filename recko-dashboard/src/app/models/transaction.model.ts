import { ICompanyCredential } from './company-credential.model';

export interface ITransaction {
    id: string;
    accountId: string;
    holder: string;
    type: string;
    amount: number;
    date: Date;
    credential: ICompanyCredential;
}