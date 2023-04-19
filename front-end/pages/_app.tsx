import * as React from 'react';
import Head from 'next/head';
import { type AppProps } from 'next/app';
import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { CacheProvider, type EmotionCache } from '@emotion/react';
import theme from '../src/theme';
import createEmotionCache from '../src/createEmotionCache';
import { AuthContextProvider } from '../src/contexts/AuthContext';
import { SnackbarProvider } from 'notistack';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { routes } from '../src/config/AppConfig';
import { useRouter } from 'next/router';
import { LoggedLayout, UnloggedLayout } from '../src/layouts';

// Client-side cache, shared for the whole session of the user in the browser.
const clientSideEmotionCache = createEmotionCache();

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 3,
      staleTime: 1000 * 60 * 5, // 5 minutes
    },
  },
});

export interface MyAppProps extends AppProps {
  emotionCache?: EmotionCache;
}

const openRoutes = [routes.home, routes.login, routes.register, routes.forgotPassword];

export default function MyApp(props: MyAppProps) {
  const router = useRouter();
  const path = router.pathname;
  const { Component, emotionCache = clientSideEmotionCache, pageProps } = props;
  return (
    <QueryClientProvider client={queryClient}>
      <CacheProvider value={emotionCache}>
        <Head>
          <meta name="viewport" content="initial-scale=1, width=device-width" />
        </Head>
        <ThemeProvider theme={theme}>
          {/* CssBaseline kickstart an elegant, consistent, and simple baseline to build upon. */}
          <CssBaseline />
          <SnackbarProvider
            anchorOrigin={{
              vertical: 'top',
              horizontal: 'center',
            }}
          />
          <AuthContextProvider>
            {openRoutes.includes(path) ? (
              <UnloggedLayout>
                <Component {...pageProps} />
              </UnloggedLayout>
            ) : (
              <LoggedLayout>
                <Component {...pageProps} />
              </LoggedLayout>
            )}
          </AuthContextProvider>
        </ThemeProvider>
      </CacheProvider>
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  );
}
