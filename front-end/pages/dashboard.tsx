import React from 'react';
import { Paper, Typography, Stack, Button, Divider } from '@mui/material';
import Link from '../src/components/Link';
import { routes } from '../src/config/AppConfig';
import { useAccount, useActivities } from '../src/services/Account';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';
import { BalanceCard } from '../src/components/BalanceCard';
import { ActivityListItem } from '../src/components/ActivityListItem';

const Dashboard = () => {
  const account = useAccount();
  const activities = useActivities({
    accountId: account.data?.id,
    size: 5,
  });
  return (
    <>
      <BalanceCard />
      <Stack direction="row" justifyContent="space-between" gap={2} mt={2}>
        <Link href={routes.send} sx={{ width: '100%' }} underline="none">
          <Paper
            sx={{
              py: 3,
              bgcolor: (theme) => theme.palette.secondary.dark,
            }}
          >
            <Typography variant="body1" align="center">
              Transferir valor
            </Typography>
          </Paper>
        </Link>
        <Link href={routes.attachMoney.index} sx={{ width: '100%' }} underline="none">
          <Paper
            sx={{
              py: 3,
              bgcolor: (theme) => theme.palette.secondary.dark,
            }}
          >
            <Typography variant="body1" align="center">
              Adicionar valor
            </Typography>
          </Paper>
        </Link>
      </Stack>

      <Paper
        component={Stack}
        divider={<Divider />}
        sx={{
          mt: 2,
          p: 3.5,
          flexGrow: 1,
          borderRadius: 2,
          flexDirection: 'column',
        }}
      >
        <Typography variant="h6">Suas atividades</Typography>
        <Divider />
        {activities.isLoading && <Typography mt={2}>Carregando...</Typography>}
        {activities.isError && <Typography mt={2}>Erro ao carregar atividades</Typography>}
        {activities.isSuccess && activities.data.data.length === 0 && (
          <Typography mt={2}>Nenhuma atividade encontrada</Typography>
        )}
        {activities.data?.data.map((activity) => (
          <ActivityListItem key={activity.uuid} activity={activity} />
        ))}
        <Button
          LinkComponent={Link}
          href={routes.activities}
          variant="text"
          sx={{ alignSelf: 'flex-end', mt: 2 }}
          endIcon={<ArrowForwardIcon />}
        >
          Ver todas
        </Button>
      </Paper>
    </>
  );
};

export default Dashboard;
