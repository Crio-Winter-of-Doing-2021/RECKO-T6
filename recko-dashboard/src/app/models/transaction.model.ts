import { IPartnerCredential } from './parther-credential.model';

export interface ITransaction {
    id: string;
    accountId: string;
    holderName: string;
    receiver: string;
    type: string;
    amount: number;
    date: Date;
    credential: IPartnerCredential;
}