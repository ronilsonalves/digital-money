export type ApiError = {
  timeStamp: Date;
  statusCode: 0;
  message: string;
};

export type User = {
  id: string;
  name: string;
  lastName: string;
  cpf: string;
  email: string;
  phone: string;
  accountNumber: string;
};

export type GetAllResquest = {
  page: number;
  size: number;
  sort?: string;
  direction?: 'ASC' | 'DESC';
};
