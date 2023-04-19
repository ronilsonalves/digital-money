import React from 'react';
import { useAccount, useActivities } from '../../src/services/Account';
import { CircularProgress, Stack, Typography, Button, Paper, Divider, List, Pagination } from '@mui/material';
import { ActivityListItem } from '../../src/components/ActivityListItem';

const Activities = () => {
  const account = useAccount();
  const [page, setPage] = React.useState(0);
  const activities = useActivities({
    accountId: account.data?.id,
    page,
    size: 20,
  });

  if (account.isError) {
    return <div>Error</div>;
  }

  if (account.isLoading || activities.isLoading) {
    return (
      <Stack
        direction={'row'}
        alignItems={'center'}
        justifyContent={'center'}
        sx={{
          position: 'absolute',
          bottom: 0,
          left: 0,
          right: 0,
          top: 0,
        }}
      >
        <CircularProgress sx={{ fontSize: 100 }} />
      </Stack>
    );
  }

  return (
    <>
      <Paper sx={{ maxHeight: '100%', display: 'flex', flexDirection: 'column' }}>
        <Stack direction={'row'} justifyContent={'space-between'} mb={1} px={3} py={1.5}>
          <Typography variant="h5" fontWeight={'bold'}>
            Suas Atividades
          </Typography>
          <Button variant="text" color="primary">
            Filtrar
          </Button>
        </Stack>
        {activities.isLoading && <Typography mt={2}>Carregando...</Typography>}
        {activities.isError && <Typography mt={2}>Erro ao carregar atividades</Typography>}
        <List
          component={Stack}
          divider={<Divider />}
          sx={{
            overflowY: 'auto',
            minHeight: 0,
            '&::-webkit-scrollbar': {
              width: '0.4em',
            },
            '&::-webkit-scrollbar-thumb': {
              backgroundColor: 'secondary.main',
              outline: '1px solid slategrey',
              borderRadius: 2,
            },
          }}
        >
          {activities.isSuccess && activities.data.data.length === 0 && (
            <Typography mt={2}>Nenhuma atividade encontrada</Typography>
          )}
          {activities.isSuccess &&
            activities.data.data.map((activity) => <ActivityListItem key={activity.uuid} activity={activity} />)}
        </List>
        <Pagination
          size="large"
          showFirstButton
          showLastButton
          shape="rounded"
          count={activities.data?.totalPages}
          page={page + 1}
          onChange={(e, page) => {
            setPage(page - 1);
          }}
          sx={{ mt: 2, mb: 1, marginInline: 'auto' }}
        />
      </Paper>
    </>
  );
};

export default Activities;
