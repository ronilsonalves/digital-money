import React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import Button from '@mui/material/Button';
import Image from 'next/image';
import Link from '../components/Link';
import { Stack, useTheme, useMediaQuery } from '@mui/material';

interface UnloggedLayoutProps {
  children: React.ReactNode;
}

export const UnloggedLayout = ({ children }: UnloggedLayoutProps) => {
  const theme = useTheme();
  const isSmallScreen = useMediaQuery(theme.breakpoints.down('sm'));

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        minHeight: '100dvh',
      }}
    >
      <AppBar position="static" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}>
        <Container maxWidth="xl">
          <Toolbar disableGutters>
            <Link
              href="/"
              sx={{
                width: { xs: 63, md: 87 },
                height: { xs: 25, md: 33 },
                position: 'relative',
              }}
            >
              <Image src="/images/logo.svg" alt="logo" fill />
            </Link>

            <Box ml="auto">
              <Button variant="outlined" size={isSmallScreen ? 'small' : 'large'} LinkComponent={Link} href="/sign-in">
                Entrar
              </Button>
              <Button
                variant="contained"
                color="primary"
                size={isSmallScreen ? 'small' : 'large'}
                sx={{ ml: { xs: 1, md: 4 } }}
                LinkComponent={Link}
                href="/sign-up"
              >
                Criar conta
              </Button>
            </Box>
          </Toolbar>
        </Container>
      </AppBar>

      <Stack flexGrow={1}>{children}</Stack>

      <Stack
        sx={{
          bgcolor: 'background.paper',
          height: 60,
          mt: 'auto',
          zIndex: (theme) => theme.zIndex.drawer + 1,
        }}
        justifyContent={'center'}
      >
        <Container maxWidth="xl">
          <Typography variant="body2" color="primary.main" align="center">
            © 2022 Digital Money House
            {new Date().getFullYear()}
            {'.'}
          </Typography>
        </Container>
      </Stack>
    </Box>
  );
};
