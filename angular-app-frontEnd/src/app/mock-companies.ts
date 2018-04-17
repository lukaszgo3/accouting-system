import { Company } from './company';

export const COMPANIES: Company[] = [
    {
        companyId: 1,
        name: 'GreenTeam',
        issueDate: '10.10.2020',
        address: 'Dworcowa',
        city: 'Zakopane',
        zipCode: '99-999',
        nip: '444 555 666',
        bankAccoutNumber: '4444555566667777888899990000',
        taxType: 'LINEAR',
        personalCarUsage: false
    },
    {
        companyId: 2,
        name: 'Good Company',
        issueDate: '10.10.2019',
        address: 'Zelazna',
        city: 'Wąchock',
        zipCode: '99-777',
        nip: '111 222 333',
        bankAccoutNumber: '12345698745632145625478888',
        taxType: 'LINEAR',
        personalCarUsage: true
    },
    {
        companyId: 3,
        name: 'Police Company',
        issueDate: '10.10.2010',
        address: 'Policyjna',
        city: 'Sosnowiec',
        zipCode: '11-999',
        nip: '000 999 7777',
        bankAccoutNumber: '444455556744144557885465000',
        taxType: 'LINEAR',
        personalCarUsage: false
    }
]


