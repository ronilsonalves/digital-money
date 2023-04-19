import React from 'react';
import { Card, CardHeader, CardContent, Typography, Stack, Divider, Paper } from '@mui/material';
import { useAuth } from '../src/contexts/AuthContext';
import { ProfileDetailsRow } from '../src/components/ProfileDetailsRow';
import Link from '../src/components/Link';
import { routes } from '../src/config/AppConfig';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';
import { AccountCard } from '../src/components/AccountCard';

const Profile = () => {
  const { user, signOut } = useAuth();

  if (user == null) {
    void signOut();
    return null;
  }

  return (
    <>
      <Card elevation={3}>
        <CardHeader title="Seus dados" titleTypographyProps={{ fontWeight: 700 }} />
        <CardContent>
          <Stack gap={2} divider={<Divider />}>
            <ProfileDetailsRow label="E-mail" value={user.email} />
            <ProfileDetailsRow label="Nome" value={user.name} />
            <ProfileDetailsRow label="Sobrenome" value={user.lastName} />
            <ProfileDetailsRow label="CPF" value={user.cpf} />
            <ProfileDetailsRow label="Telefone" value={user.phone} />
            <ProfileDetailsRow label="Senha" value={'*****'} />
          </Stack>
        </CardContent>
      </Card>

      <Paper elevation={3} sx={{ my: 3, bgcolor: 'primary.light' }}>
        <Stack
          component={Link}
          href={routes.cards}
          underline="none"
          justifyContent={'space-between'}
          alignItems={'center'}
          direction={'row'}
          color={'common.black'}
          spacing={2}
          sx={{ p: 2 }}
        >
          <Typography variant="h6" fontWeight={700}>
            Administrar meios de pagamentos
          </Typography>
          <ArrowForwardIcon />
        </Stack>
      </Paper>

      <AccountCard />
    </>
  );
};

export default Profile;
