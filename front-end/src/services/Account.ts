import {
  type Card,
  type Account,
  type ActivitiesRequest,
  type ActivitiesResponse,
  type CardCreateRequest,
  type CreateTransactionRequest,
  type ActivityRequest,
  type Activity,
} from '../types/accounts';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { getAPIClientAccount } from './api';

export const useRecentTransfers = (accountId?: string) => {
  const api = getAPIClientAccount();
  return useQuery({
    queryKey: ['recent-transfers'],
    queryFn: async () => await api.get<ActivitiesResponse>(`api/accounts/${accountId ?? ''}/recent-transfers`),
    enabled: !!accountId,
    select: (res) => res.data,
  });
};

export const useActivity = ({ accountId, activityId }: ActivityRequest) => {
  const api = getAPIClientAccount();
  return useQuery({
    queryKey: ['activity', accountId, activityId],
    queryFn: async () => await api.get<Activity>(`api/accounts/${accountId ?? ''}/activity/${activityId ?? ''}`),
    enabled: !!accountId && !!activityId,
    select: (res) => res.data,
  });
};

export const useActivities = ({ accountId, ...data }: ActivitiesRequest) => {
  const api = getAPIClientAccount();
  return useQuery({
    queryKey: ['activities', accountId, data],
    queryFn: async () =>
      await api.get<ActivitiesResponse>(`api/accounts/${accountId ?? ''}/activity`, { params: data }),
    enabled: !!accountId,
    select: (res) => res.data,
    keepPreviousData: true,
  });
};

export const useCreateTransaction = () => {
  const queryClient = useQueryClient();
  const api = getAPIClientAccount();
  return useMutation({
    mutationFn: async ({ originAccountNumber, ...data }: CreateTransactionRequest) =>
      await api.post<Activity>(`api/accounts/${originAccountNumber}/transactions`, { originAccountNumber, ...data }),
    onSuccess: async () => {
      await queryClient.invalidateQueries(['activities']);
      await queryClient.invalidateQueries(['account']);
    },
  });
};

export const useAccount = () => {
  const api = getAPIClientAccount();
  return useQuery({
    queryKey: ['account'],
    queryFn: async () => await api.get<Account>(`api/accounts`),
    select: (res) => res.data,
  });
};

export const useCards = (accountId?: string) => {
  const api = getAPIClientAccount();
  return useQuery({
    queryKey: ['cards', accountId],
    queryFn: async () => await api.get<Card[]>(`api/accounts/${accountId ?? ''}/cards`),
    enabled: !!accountId,
    select: (res) => res.data,
  });
};

export const useCard = (accountId?: string, cardId?: string) => {
  const api = getAPIClientAccount();
  return useQuery({
    queryKey: ['card', accountId, cardId],
    queryFn: async () => await api.get<Card>(`api/accounts/${accountId ?? ''}/cards/${cardId ?? ''}`),
    enabled: !!accountId && !!cardId,
    select: (res) => res.data,
  });
};

export const useCreateCard = () => {
  const queryClient = useQueryClient();
  const api = getAPIClientAccount();
  return useMutation({
    mutationFn: async ({ accountId, ...data }: CardCreateRequest) =>
      await api.post<Card>(`api/accounts/${accountId}/cards`, data),
    onSuccess: async (_, { accountId }) => {
      await queryClient.invalidateQueries(['cards', accountId]);
    },
  });
};

export const useDeleteCard = () => {
  const queryClient = useQueryClient();
  const api = getAPIClientAccount();
  return useMutation({
    mutationFn: async ({ accountId, cardId }: { accountId: string; cardId: string }) =>
      await api.delete<Card>(`api/accounts/${accountId}/cards/${cardId}`),
    onSuccess: async (_, { accountId }) => {
      await queryClient.invalidateQueries(['cards', accountId]);
    },
  });
};

export interface TransactionAccount {
  accountOwner: string;
  recipientAccountNumber: string;
  transactionDate: string;
}

export const useLastFiveTransfers = (accountId?: string) => {
  const api = getAPIClientAccount();
  return useQuery({
    queryKey: ['last-five-accounts'],
    queryFn: async () => await api.get<TransactionAccount[]>(`api/accounts/${accountId ?? ''}/transfers/recent`),
    enabled: !!accountId,
    select: (res) => res.data,
  });
};

export const useDownloadRecipt = () => {
  const api = getAPIClientAccount();
  return useMutation({
    mutationFn: async ({ accountId, activityId }: { accountId: string; activityId: string }) =>
      await api.get(`api/accounts/${accountId ?? ''}/activity/${activityId ?? ''}/receipt`, {
        responseType: 'blob',
      }),
    onSuccess: async (res) => {
      const url = window.URL.createObjectURL(new Blob([res.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'recibo.pdf');
      document.body.appendChild(link);
      link.click();
    },
  });
};
