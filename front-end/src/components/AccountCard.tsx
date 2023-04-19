import { Box, Card, CardContent, Stack, Typography } from '@mui/material';
import React from 'react';
import { ClipboardButton } from './ClipboardButton';
import { useAuth } from '../contexts/AuthContext';

export const AccountCard = () => {
  const { user, signOut } = useAuth();

  if (user == null) {
    void signOut();
    return null;
  }

  return (
    <Card sx={{ bgcolor: 'background.default' }} elevation={3}>
      <CardContent>
        <Typography mb={2}>
          Copiar o numero de sua conta para adicionar ou transferir valor a partir de outra conta
        </Typography>
        <Stack direction={'row'} justifyContent={'space-between'} alignItems={'center'}>
          <Box>
            <Typography variant="h6" fontWeight={700} color={'primary'}>
              Conta n°:{' '}
            </Typography>
            <Typography>{user.accountNumber}</Typography>
          </Box>
          <ClipboardButton size="large" value={user.accountNumber} />
        </Stack>
      </CardContent>
    </Card>
  );
};
