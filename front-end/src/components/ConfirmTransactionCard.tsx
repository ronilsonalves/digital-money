import React from 'react';
import { Button, Card, CardActions, CardContent, Typography } from '@mui/material';
import { LoadingButton } from '@mui/lab';
import { formatCurrency } from '../utils/parser';
interface ConfirmTransactionCardProps {
  amount: number;
  accountNumber?: string;
  cardNumber?: string;
  date?: Date | null;
  description?: string;
  loading?: boolean;
  onBack?: () => void;
  onConfirm?: () => void;
}
export const ConfirmTransactionCard = ({
  amount,
  cardNumber,
  accountNumber,
  loading,
  onBack,
  onConfirm,
}: ConfirmTransactionCardProps) => {
  return (
    <Card variant="outlined">
      <CardContent>
        <Typography variant="h5" fontWeight={700}>
          Revise se está tudo correto
        </Typography>
        <Typography variant="body1" mt={2}>
          Vai transferir:
        </Typography>
        <Typography variant="h6" color="success.main" fontWeight={700}>{`${formatCurrency(amount)}`}</Typography>
        <Typography variant="caption">{cardNumber ? `Cartão ${cardNumber}` : 'Saldo da conta'}</Typography>
        <Typography variant="body1" mt={2}>
          Para Conta:
        </Typography>
        <Typography component={'span'} fontWeight={700}>
          {accountNumber ? ` ${accountNumber}` : 'Própria'}
        </Typography>
      </CardContent>
      <CardActions sx={{ justifyContent: 'space-between' }}>
        <Button variant="outlined" onClick={onBack} disabled={loading}>
          Voltar
        </Button>
        <LoadingButton variant="contained" color="primary" onClick={onConfirm} loading={loading}>
          Confirmar
        </LoadingButton>
      </CardActions>
    </Card>
  );
};
