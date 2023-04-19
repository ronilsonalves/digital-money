import Router, { useRouter } from 'next/router';
import React from 'react';
import { useAccount, useActivity } from '../../src/services/Account';
import Typography from '@mui/material/Typography';

const Details = () => {
  const id = useRouter().query.id as string | undefined;
  const { data } = useAccount();
  const activity = useActivity({ accountId: data?.id, activityId: id });
  if (!id) {
    void Router.push('/activities');
  }

  return (
    <>
      {activity.isLoading && <Typography mt={2}>Carregando...</Typography>}
      {activity.isError && (
        <>
          <Typography variant="h6" mt={2}>
            Erro ao carregar atividade
          </Typography>
          <Typography mt={2}>{(activity.error as any)?.message}</Typography>
        </>
      )}
      {activity.isSuccess && (
        <>
          <Typography variant="h6" mt={2}>
            {activity.data.description}
          </Typography>
          <Typography mt={2}>{activity.data.transactionType}</Typography>
          <Typography mt={2}>{activity.data.transactionAmount}</Typography>
          <Typography mt={2}>{activity.data.transactionDate}</Typography>
        </>
      )}
    </>
  );
};

export default Details;
