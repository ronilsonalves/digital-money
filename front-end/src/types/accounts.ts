type TransactionType = 'DEPÓSITO' | 'TRANSFERÊNCIA' | 'PAGAMENTO';
type ActivityType = 'RECEITA' | 'DESPESA' | 'AMBOS';
export interface Activity {
  uuid: string;
  originAccountNumber: string;
  cardEnding: string;
  recipientAccountNumber: string;
  transactionAmount: number;
  transactionDate: string;
  transactionType: TransactionType;
  description: string;
}

export interface ActivitiesRequest {
  accountId?: string;
  page?: number;
  size?: number;
  sort?: keyof Activity;
  direction?: 'ASC' | 'DESC';
  startDate?: string;
  endDate?: string;
  transactionType?: TransactionType;
  activityType?: ActivityType;
  minAmount?: number;
  maxAmount?: number;
}

export interface ActivitiesResponse {
  totalItems: number;
  totalPages: number;
  currentPage: number;
  data: Activity[];
}

export interface ActivityRequest {
  accountId?: string;
  activityId?: string;
}

export interface Account {
  available_amount: number;
  id: string;
  user_id: string;
}

export interface Card {
  id: string;
  number: number;
  cvc: number;
  account_id: string;
  expiration_date: string;
  first_last_name: string;
}

export interface CardCreateRequest {
  accountId: string;
  number: string;
  cvc: string;
  expiration_date: string;
  first_last_name: string;
}

export interface CreateTransactionRequest {
  originAccountNumber: string;
  cardIdentification?: string;
  recipientAccountNumber: string;
  transactionAmount: number;
  transactionDate: string;
  transactionType: 'DEPÓSITO' | 'TRANSFERÊNCIA' | 'PAGAMENTO';
  description?: string;
}
