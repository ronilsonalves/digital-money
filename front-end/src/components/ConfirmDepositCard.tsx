import React from 'react';
import { Card, CardContent, Typography } from '@mui/material';
import { type Card as CardType } from '../types/accounts';
interface ComfirmDepositCardProps {
  amount: string;
  card?: CardType;
}
export const ConfirmDepositCard = ({ amount, card }: ComfirmDepositCardProps) => {
  return (
    <Card variant="outlined">
      <CardContent>
        <Typography fontWeight={700}>Revise se está tudo correto</Typography>
        <Typography variant="body1" mt={2}>
          Vai transferir:
        </Typography>
        <Typography fontWeight={700}>{`R$ ${amount}`}</Typography>
        <Typography variant="body1" mt={2}>
          Para:
        </Typography>
        <Typography component={'span'} fontWeight={700}>
          Conta própria
        </Typography>

        <Typography variant="body1" mt={2}>
          {'Cartão:'}
        </Typography>
        <Typography fontWeight={700} ml={1}>
          {`**** **** **** ${card?.number.toString().slice(-4) ?? '*****'}`}
        </Typography>
      </CardContent>
    </Card>
  );
};
