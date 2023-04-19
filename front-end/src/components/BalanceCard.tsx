/* eslint-disable @typescript-eslint/naming-convention */
import React from 'react';
import { Paper, Typography, Stack, Box, CircularProgress } from '@mui/material';
import { ClipboardButton } from './ClipboardButton';
import { routes } from '../config/AppConfig';
import Link from './Link';
import { formatCurrency } from '../utils/parser';
import { useAccount } from '../services/Account';

export const BalanceCard = () => {
  const { data, ...account } = useAccount();
  return (
    <Paper
      sx={{
        p: 3.5,
        display: 'flex',
        flexGrow: 1,
        borderRadius: 2,
        flexDirection: 'column',
      }}
    >
      {account.isLoading && (
        <Stack alignItems={'center'}>
          <CircularProgress sx={{ mt: 2 }} size={80} />
        </Stack>
      )}
      {account.isError && <Typography mt={2}>Erro ao carregar conta</Typography>}
      {account.isSuccess && data === undefined && <Typography mt={2}>Nenhuma conta encontrada</Typography>}
      {account.isSuccess && data !== undefined && (
        <>
          <Stack direction="row" justifyContent="space-between" mb={1.5}>
            <Stack direction="row" alignItems="center">
              <Typography variant="body1">Conta:</Typography>
              <Typography variant="body1" color={'primary'} fontWeight={'bold'} sx={{ ml: 1 }}>
                {' '}
                {data.id}
              </Typography>
              <ClipboardButton value={data.id} />
            </Stack>
            <Link href={routes.cards}>
              <Typography variant="body2">Ver cartões</Typography>
            </Link>
          </Stack>
          <Typography variant="body1">Dinheiro disponível</Typography>
          <Box
            sx={{
              alignSelf: 'flex-start',
              mt: 1,
              border: (theme) => `1px solid ${theme.palette.primary.main}`,
              borderRadius: 100,
              p: 0.5,
              px: 2,
            }}
          >
            <Typography fontSize={36}>{formatCurrency(data.available_amount)} </Typography>
          </Box>
        </>
      )}
    </Paper>
  );
};
