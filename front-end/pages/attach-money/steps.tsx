import React from 'react';
import Stepper from '@mui/material/Stepper';
import Step from '@mui/material/Step';
import StepLabel from '@mui/material/StepLabel';
import Button from '@mui/material/Button';
import {LoadingButton} from '@mui/lab';
import Typography from '@mui/material/Typography';
import { SelectCardOption } from '../../src/components/SelectCardOption';
import { Card, CardActions } from '@mui/material';
import { enqueueSnackbar } from 'notistack';
import { SelectAmountCard } from '../../src/components/SelectAmountCard';
import { ConfirmDepositCard } from '../../src/components/ConfirmDepositCard';
import { useAccount, useCreateTransaction } from '../../src/services/Account';
import { routes } from '../../src/config/AppConfig';
import Router from 'next/router';
import { type Card as CardType } from '../../src/types/accounts';

const steps = ['Selecione o cartão', 'Selecione valor', 'Confirme'];

const Steps = () => {
  const account = useAccount();
  const createTransaction = useCreateTransaction();
  const [activeStep, setActiveStep] = React.useState(0);
  const [selectedCard, setSelectedCard] = React.useState<CardType>();
  const [amount, setAmount] = React.useState('0');

  const handleNext = () => {
    if (activeStep === 0 && !selectedCard) {
      enqueueSnackbar('Selecione um cartão', { variant: 'error' });
      return;
    }
    if (activeStep === 1 && amount === '0') {
      enqueueSnackbar('Selecione um valor', { variant: 'error' });
      return;
    }

    if (activeStep === 2) {
      if (!account.data?.id) {
        enqueueSnackbar('Conta não encontrada', { variant: 'error' });
        return;
      }

      createTransaction.mutate(
        {
          // eslint-disable-next-line @typescript-eslint/no-non-null-assertion, @typescript-eslint/no-non-null-asserted-optional-chain
          cardIdentification: selectedCard?.id!,
          transactionAmount: Number(amount),
          originAccountNumber: account.data.id,
          recipientAccountNumber: account.data.id,
          transactionDate: new Date().toISOString(),
          transactionType: 'DEPÓSITO',
        },
        {
          onSuccess: (data) => {
            enqueueSnackbar('Depósito realizado com sucesso', { variant: 'success' });
            console.log(data);
            void Router.push({
              pathname: routes.attachMoney.success,
              query: { id: data.data.uuid },
            });
          },
          onError: (err) => {
            console.log((err as any).response.data);
            enqueueSnackbar('Erro ao realizar depósito', { variant: 'error' });
          },
        },
      );
    }

    setActiveStep((prevActiveStep) => {
      if (prevActiveStep === steps.length - 1) {
        return prevActiveStep;
      }
      return prevActiveStep + 1;
    });
  };

  const handleBack = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1);
  };

  const getActiveStepContent = (step: number) => {
    switch (step) {
      case 0:
        return <SelectCardOption onSelectCard={setSelectedCard} selected={selectedCard} />;
      case 1:
        return (
          <SelectAmountCard title={'Quanto deseja depositar na sua conta?'} onValueChange={setAmount} value={amount} />
        );
      case 2:
        return <ConfirmDepositCard amount={amount} card={selectedCard} />;
      default:
        return <>Unknown step</>;
    }
  };

  return (
    <>
      <Stepper activeStep={activeStep}>
        {steps.map((label) => {
          const stepProps: { completed?: boolean } = {};
          return (
            <Step key={label} {...stepProps}>
              <StepLabel>{label}</StepLabel>
            </Step>
          );
        })}
      </Stepper>
      {activeStep === steps.length ? (
        <React.Fragment>
          <Typography sx={{ mt: 2, mb: 1 }}>All steps completed - you&apos;re finished</Typography>
        </React.Fragment>
      ) : (
        <Card sx={{ mt: 2 }}>
          {getActiveStepContent(activeStep)}
          <CardActions sx={{ p: 2 }}>
            {activeStep === 0 ? null : (
              <Button disabled={createTransaction.isLoading} onClick={handleBack}>
                Voltar
              </Button>
            )}
            <LoadingButton
              loading={createTransaction.isLoading}
              variant="contained"
              onClick={handleNext}
              sx={{ ml: 'auto !important' }}
            >
              {activeStep === steps.length - 1 ? 'Finalizar' : 'Continuar'}
            </LoadingButton>
          </CardActions>
        </Card>
      )}
    </>
  );
};

export default Steps;
