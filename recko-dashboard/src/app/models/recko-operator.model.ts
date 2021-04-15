import { ICompany } from "./company.model";

export interface IReckoOperator {
    username: string;
    password: string;
    company: ICompany;
}