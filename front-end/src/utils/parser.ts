import { parse } from 'date-fns';
import { type User } from '../types/shared';

export const getFullName = (user: Pick<User, 'name' | 'lastName'>) => `${user.name} ${user.lastName}`;

export const formatCurrency = (value: number) => value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

export const getNiceDate = (date?: string) => {
  if (!date) return '';
  const parsedDate = parse(date, 'yyyy-MM-dd', new Date());
  return parsedDate.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric' });
};
