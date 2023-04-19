import React, { useEffect } from 'react';
import { createContext, type ReactNode, useState, useContext } from 'react';
import { useGetMe, UseSignIn } from '../services/auth';
import { destroyCookie, parseCookies, setCookie } from 'nookies';
import Router from 'next/router';
import { type AxiosError } from 'axios';
import { type ApiError, type User } from '../types/shared';
import { enqueueSnackbar } from 'notistack';
import jwtDecode from 'jwt-decode';

// create Auth context and provider
// eslint-disable-next-line @typescript-eslint/consistent-type-assertions
const AuthContext = createContext({} as AuthContextType);

export const useAuth = () => useContext(AuthContext);

interface AuthContextProviderProps {
  children: ReactNode;
}

interface SignInCredentials {
  email: string;
  password: string;
}

interface AuthContextType {
  signIn: (credentials: SignInCredentials) => Promise<void>;
  signOut: () => Promise<void>;
  isAuthenticated: boolean;
  user: User | undefined;
  isLoading: boolean;
}

export function AuthContextProvider({ children }: AuthContextProviderProps) {
  const { 'nextauth.token': token } = parseCookies();
  const { mutateAsync: singInMutation } = UseSignIn();
  const [enabled, setEnabled] = useState(!!token);
  const { data: user, isLoading, ...getMe } = useGetMe({ enabled });
  const isAuthenticated = !!user;

  useEffect(() => {
    if (token) {
      setEnabled(true);
    } else {
      setEnabled(false);
      void signOut();
    }
  }, [token]);

  async function signIn({ email, password }: SignInCredentials) {
    try {
      const { data } = await singInMutation({
        email,
        password,
      });

      const decode = jwtDecode<{ exp: number }>(data.token.replace('Bearer ', ''));

      setCookie(undefined, 'nextauth.token', data.token, {
        maxAge: decode.exp * 1000,
      });
      setEnabled(true);
      await Router.push('/dashboard');
    } catch (err) {
      const error = err as AxiosError<ApiError>;
      console.log(error.response?.data.message ?? error.message);
      enqueueSnackbar(error.response?.data.message ?? error.message, { variant: 'error' });
    }
  }

  async function signOut() {
    setEnabled(false);

    destroyCookie(undefined, 'nextauth.token');
    getMe.remove();

    await Router.push('/');
  }

  return (
    <AuthContext.Provider value={{ isAuthenticated, user, isLoading, signIn, signOut }}>
      {children}
    </AuthContext.Provider>
  );
}
