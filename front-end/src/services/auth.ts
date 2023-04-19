import { useMutation, useQuery } from '@tanstack/react-query';
import { getAPIClientUsers } from './api';
import { type User } from '../types/shared';

interface SingInRequest {
  email: string;
  password: string;
}

interface SingInResponse {
  token: string;
  user: User;
}

interface SingUpRequest {
  name: string;
  lastname: string;
  email: string;
  password: string;
  phone: string;
  cpf: string;
}

export function UseSignIn() {
  const api = getAPIClientUsers();
  return useMutation({
    mutationFn: async (data: SingInRequest) =>
      await api<SingInResponse>({
        method: 'POST',
        url: '/auth/login',
        data,
      }),
  });
}

export function UseSignUp() {
  const api = getAPIClientUsers();
  return useMutation({
    mutationFn: async (data: SingUpRequest) =>
      await api<null>({
        method: 'POST',
        url: '/users/register',
        data,
      }),
  });
}

export function useGetMe({ ctx, enabled }: { ctx?: any; enabled: boolean }) {
  const api = getAPIClientUsers(ctx);
  return useQuery({
    queryKey: ['me'],
    queryFn: async () => await api.get<User>('/users/me'),
    select: (res) => res.data,
    enabled,
  });
}
