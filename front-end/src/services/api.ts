import axios from 'axios';
import { parseCookies, destroyCookie } from 'nookies';
import { useAuth } from '../contexts/AuthContext';
import { enqueueSnackbar } from 'notistack';

export const getAPIClientUsers = (ctx?: any) => {
  const { signOut } = useAuth();
  const api = axios.create({
    baseURL: 'http://localhost:8081/',
  });

  api.interceptors.request.use((config) => {
    const { 'nextauth.token': token } = parseCookies(ctx);

    if (token?.length > 0) {
      config.headers.Authorization = token;
    }

    return config;
  });

  api.interceptors.response.use(undefined, async (error) => {
    if (error.response.status === 401) {
      destroyCookie(ctx, 'nextauth.token');
      if (window !== undefined) {
        void signOut?.();
        enqueueSnackbar('Sua sessão expirou, faça login novamente', {
          variant: 'error',
        });
      } else {
        ctx.res.writeHead(302, { Location: '/' });
        ctx.res.end();
      }
    }
    return await Promise.reject(error);
  });

  return api;
};

export const getAPIClientAccount = (ctx?: any) => {
  const api = axios.create({
    baseURL: 'http://localhost:8082/',
  });

  api.interceptors.request.use((config) => {
    const { 'nextauth.token': token } = parseCookies(ctx);

    if (token?.length > 0) {
      config.headers.Authorization = token;
    }

    return config;
  });

  api.interceptors.response.use(undefined, async (error) => {
    if (error.response.status === 401) {
      destroyCookie(ctx, 'nextauth.token');
      if (window !== undefined) {
        window.location.href = '/';
      } else {
        ctx.res.writeHead(302, { Location: '/' });
        ctx.res.end();
      }
    }
    return await Promise.reject(error);
  });

  return api;
};
