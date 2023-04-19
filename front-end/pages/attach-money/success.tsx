import React from 'react';
import Paper from '@mui/material/Paper';
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import Typography from '@mui/material/Typography';
import { Button, type ButtonProps, Stack, styled } from '@mui/material';
import { grey } from '@mui/material/colors';
import { useRouter } from 'next/router';
import { useAccount, useActivity, useDownloadRecipt } from '../../src/services/Account';
import { formatCurrency, getNiceDate } from '../../src/utils/parser';
import Link from '../../src/components/Link';
import { routes } from '../../src/config/AppConfig';

const Success = () => {
  const router = useRouter();
  const account = useAccount();
  const id = router.query.id as string | undefined;
  const transaction = useActivity({
    accountId: account.data?.id,
    activityId: id,
  });
  const downloadReceipt = useDownloadRecipt();

  const handleDownload = () => {
    if (transaction.data && account.data) {
      downloadReceipt.mutate({
        accountId: account.data.id,
        activityId: transaction.data.uuid,
      });
    }
  };

  return (
    <>
      <Paper
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          p: 2.5,
          mt: 3,
          gap: 2,
          bgcolor: 'primary.dark',
        }}
      >
        <CheckCircleOutlineIcon sx={{ fontSize: 100, color: 'success.dark' }} />
        <Typography variant="h5" fontWeight={700}>
          Depósito realizado com sucesso!
        </Typography>
      </Paper>

      {transaction.data && (
        <Paper
          sx={{
            display: 'flex',
            flexDirection: 'column',
            bgcolor: 'background.default',
            mt: 3,
            p: 2.5,
          }}
        >
          <Typography variant="h6" fontWeight={700}>
            Depósito realizado com sucesso!
          </Typography>
          <Typography variant="body1" sx={{ mt: 1.5 }}>
            Criado em {getNiceDate(transaction.data.transactionDate)}
          </Typography>
          <Typography variant={'h6'} color={'success.main'} fontWeight={'bold'}>
            {formatCurrency(transaction.data.transactionAmount)}
          </Typography>
          <Typography variant="body1" mt={2}>
            Para:
          </Typography>
          <Typography fontWeight={700}>Conta própria</Typography>
        </Paper>
      )}

      <Stack direction={'row'} justifyContent={'flex-end'} gap={2.5} mt={3}>
        <GrayButton variant="contained" size="large" LinkComponent={Link} href={routes.attachMoney.index}>
          Voltar ao início
        </GrayButton>
        <Button variant="contained" size="large" onClick={handleDownload}>
          Baixar comprovante
        </Button>
      </Stack>
    </>
  );
};

export default Success;

const GrayButton = styled(Button)<ButtonProps>(({ theme }) => ({
  color: theme.palette.getContrastText(grey[500]),
  backgroundColor: grey[400],
  '&:hover': {
    backgroundColor: grey[600],
  },
}));
