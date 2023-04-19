import React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Typography from '@mui/material/Typography';
import { useAuth } from '../contexts/AuthContext';
import Link from '../components/Link';
import {
  Stack,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Icon,
  CircularProgress,
} from '@mui/material';

import { drawerList, drawerWidth } from '../config/AppConfig';
import Image from 'next/image';

export const LoggedLayout = ({ children }: { children: React.ReactNode }) => {
  /*   const theme = useTheme();
  const isSmallScreen = useMediaQuery(theme.breakpoints.down('sm')); */
  const { signOut, user, isLoading: authLoading } = useAuth();

  if (authLoading)
    return (
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          minHeight: '100dvh',
          justifyContent: 'center',
          alignItems: 'center',
        }}
      >
        <CircularProgress sx={{ fontSize: 100 }} />
      </Box>
    );

  const handleLogout = () => {
    void signOut();
  };

  if (!user) {
    return null;
  }

  return (
    <Box
      sx={{
        display: 'flex',
        flex: '1 0 0',
        flexDirection: 'column',
        maxHeight: '100dvh',
        minHeight: 0,
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
              <Stack direction={'row'} alignItems={'center'}>
                <Avatar
                  alt="User"
                  src=""
                  variant="rounded"
                  sx={{
                    width: { xs: 39, md: 43 },
                    height: { xs: 33, md: 40 },
                    bgcolor: 'primary.main',
                  }}
                >
                  {`${user.name.charAt(0)} ${user.lastName.charAt(0)}`.toUpperCase()}
                </Avatar>
                <Typography sx={{ ml: 1 }}>{`Olá, ${user.name}`}</Typography>
              </Stack>
            </Box>
          </Toolbar>
        </Container>
      </AppBar>

      <Stack flex={1} minHeight={0} direction={'row'} sx={{ overflowY: 'auto' }}>
        <Drawer
          variant="permanent"
          sx={{
            width: drawerWidth,
            flexShrink: 0,
            [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box' },
          }}
        >
          <Toolbar />
          <Box sx={{ overflow: 'auto' }}>
            <List>
              {drawerList.map(({ icon, name, path }) => (
                <ListItem key={name} disablePadding>
                  <ListItemButton LinkComponent={Link} href={path}>
                    <ListItemIcon>
                      <Icon>{icon}</Icon>
                    </ListItemIcon>
                    <ListItemText primary={name} />
                  </ListItemButton>
                </ListItem>
              ))}
              <ListItem disablePadding>
                <ListItemButton onClick={handleLogout}>
                  <ListItemIcon>
                    <Icon>{'exit_to_app'}</Icon>
                  </ListItemIcon>
                  <ListItemText primary={'Encerrar sessão'} />
                </ListItemButton>
              </ListItem>
            </List>
          </Box>
          <Typography variant="body2" color="primary.main" align="center" mt="auto" mb={1.5}>
            © 2022 Digital Money House
            {new Date().getFullYear()}
            {'.'}
          </Typography>
        </Drawer>

        <Container
          maxWidth="xl"
          sx={{
            my: 3,
            display: 'flex',
            flexDirection: 'column',
            minHeight: 0,
          }}
        >
          {children}
        </Container>
      </Stack>
    </Box>
  );
};
