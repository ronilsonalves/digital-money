import React, { useState } from 'react';
import { Card, Typography, CardContent, TextField, Stack, CardActions, Button, InputAdornment } from '@mui/material';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import br from 'date-fns/locale/pt-BR';
import { isValid } from 'date-fns';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { NumericFormat } from 'react-number-format';

import { useAccount, useCreateTransaction } from '../../src/services/Account';
import { LastFiveTransfersCard } from '../../src/components/LastFiveTransfersCard';
import { SelectRecipientAccount } from '../../src/components/SelectRecipientAccount';
import { enqueueSnackbar } from 'notistack';
import { ConfirmTransactionCard } from '../../src/components/ConfirmTransactionCard';

const SendMoney = () => {
  const account = useAccount();
  const createTransaction = useCreateTransaction();
  const [activeStep, setActiveStep] = useState(0);
  const [amount, setAmount] = useState(0);
  const [recipientAccountNumber, setRecipientAccountNumber] = useState('');
  const [transactionDate, setTransactionDate] = useState<Date | null>(new Date());
  const [description, setDescription] = useState('');

  const handleRecipientAccountNumberChange = (value: string) => {
    if (value.trim() === '') {
      enqueueSnackbar('Conta inválida', { variant: 'error' });
      return;
    }
    setRecipientAccountNumber(value);
    setActiveStep(1);
  };

  const handleNext = () => {
    if (activeStep === 1 && amount === 0) {
      enqueueSnackbar('Selecione um valor', { variant: 'error' });
      return;
    }

    if (activeStep === 1 && amount > (account.data?.available_amount ?? 0)) {
      enqueueSnackbar('Saldo insuficiente', { variant: 'error' });
      return;
    }

    if (activeStep === 1 && !isValid(transactionDate)) {
      enqueueSnackbar('Data inválida', { variant: 'error' });
      return;
    }

    setActiveStep((prev) => prev + 1);
  };

  const handleTransaction = () => {
    if (account.data)
      createTransaction.mutate({
        originAccountNumber: account.data.id,
        recipientAccountNumber,
        transactionAmount: amount,
        transactionDate: transactionDate?.toISOString() ?? new Date().toISOString(),
        description,
        transactionType: 'TRANSFERÊNCIA',
      });
  };

  const getActiveStepContent = (step: number) => {
    switch (step) {
      case 0:
        return (
          <>
            <SelectRecipientAccount value={recipientAccountNumber} onChange={handleRecipientAccountNumberChange} />
            <LastFiveTransfersCard onClick={handleRecipientAccountNumberChange} />
          </>
        );
      case 1:
        return (
          <Card variant="outlined">
            <CardContent>
              <Typography fontWeight={700} mb={2}>{`Quanto deseja transferir para: ${''}?`}</Typography>
              <NumericFormat
                customInput={TextField}
                thousandSeparator="."
                decimalSeparator=","
                decimalScale={2}
                fixedDecimalScale
                value={amount}
                onValueChange={(values) => {
                  setAmount(values.floatValue ?? 0);
                }}
                fullWidth
                InputProps={{
                  startAdornment: <InputAdornment position="start">R$</InputAdornment>,
                }}
              />
            </CardContent>
            <CardContent>
              <Typography mb={2} fontWeight={700}>
                Data de envio
              </Typography>
              <LocalizationProvider dateAdapter={AdapterDateFns} adapterLocale={br}>
                <DatePicker
                  value={transactionDate}
                  onChange={(newValue) => {
                    setTransactionDate(newValue);
                  }}
                />
              </LocalizationProvider>
            </CardContent>
            <CardContent>
              <Typography fontWeight={700} mb={2}>
                Descricão{' '}
                <Typography component="span" variant="caption" color="text.secondary" sx={{ verticalAlign: 'top' }}>
                  (opcional)
                </Typography>
              </Typography>
              <TextField
                fullWidth
                multiline
                rows={4}
                value={description}
                onChange={(e) => {
                  setDescription(e.target.value);
                }}
              />
            </CardContent>
            <CardActions>
              <Button
                variant="outlined"
                onClick={() => {
                  setActiveStep((prev) => prev - 1);
                }}
              >
                Voltar
              </Button>
              <Button variant="contained" sx={{ ml: 'auto !important' }} onClick={handleNext}>
                Continuar
              </Button>
            </CardActions>
          </Card>
        );
      case 2:
        return (
          <ConfirmTransactionCard
            amount={amount}
            accountNumber={recipientAccountNumber}
            date={transactionDate}
            description={description}
            onBack={() => {
              setActiveStep((prev) => prev - 1);
            }}
            onConfirm={handleTransaction}
            loading={createTransaction.isLoading}
          />
        );
      default:
        throw new Error('Unknown step');
    }
  };

  return (
    <Stack direction={'column'} gap={3}>
      {getActiveStepContent(activeStep)}
    </Stack>
  );
};

export default SendMoney;
