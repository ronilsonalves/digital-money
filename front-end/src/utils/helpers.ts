// function to know if money enters or leaves the account
// if recipientAccountNumber is same as accountNumber money enters the account

import { type Activity } from '../types/accounts';

// if recipientAccountNumber is different from accountNumber, leave the account
export const isMoneyIn = ({
  originAccountNumber,
  recipientAccountNumber,
}: Pick<Activity, 'originAccountNumber' | 'recipientAccountNumber'>) => {
  return recipientAccountNumber === originAccountNumber;
};
