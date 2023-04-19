import React, { forwardRef, useState, useCallback, useImperativeHandle } from 'react';
import Cards from 'react-credit-cards';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import IconButton from '@mui/material/IconButton';
import TextField from '@mui/material/TextField';
import Stack from '@mui/material/Stack';
import { Close } from '@mui/icons-material';
import 'react-credit-cards/es/styles-compiled.css';
import { useForm, Controller } from 'react-hook-form';
import { useAccount, useCreateCard } from '../services/Account';
import { enqueueSnackbar } from 'notistack';
import * as yup from 'yup';
import { yupResolver } from '@hookform/resolvers/yup';
import { type AxiosError } from 'axios';
import { type ApiError } from 'next/dist/server/api-utils';
import LoadingButton from '@mui/lab/LoadingButton';

export interface ModalHandlesRef {
  openModal: () => void;
}

interface FormValues {
  cvc: string;
  number: string;
  name: string;
  expiry: string;
}

const schema = yup.object().shape({
  cvc: yup
    .string()
    .required('CVC é obrigatório')
    .matches(/^[0-9]{3}$/, 'CVC deve ter 3 dígitos numéricos '),
  number: yup
    .string()
    .required()
    .matches(/^[0-9]+$/, 'Número do cartão deve ter apenas números')
    .matches(/^[0-9]{16}$/, 'Número do cartão deve ter 16 dígitos'),
  name: yup.string().required('Nome é obrigatório'),
  expiry: yup
    .string()
    .required()
    .matches(/^[0-9]{4}$/, 'Data de expiração deve ter 4 dígitos'),
});

const AddCardModal: React.ForwardRefRenderFunction<ModalHandlesRef> = (props, ref) => {
  const account = useAccount();
  const createCard = useCreateCard();
  const [visible, setVisible] = useState(false);
  const [focusedInput, setFocusedInput] = useState<keyof FormValues>();
  const {
    control,
    handleSubmit,
    watch,
    reset,
    formState: { errors },
  } = useForm<FormValues>({
    resolver: yupResolver(schema),
    reValidateMode: 'onBlur',
    defaultValues: {
      cvc: '',
      number: '',
      name: '',
      expiry: '',
    },
  });

  const formState = watch();

  const openModal = useCallback(() => {
    setVisible(true);
  }, []);

  useImperativeHandle(
    ref,
    () => {
      return {
        openModal,
      };
    },
    [openModal],
  );

  const handleClose = () => {
    setVisible(false);
    reset();
  };

  const onSubmit = handleSubmit((data) => {
    if (!account.data) return;

    createCard
      .mutateAsync({
        cvc: data.cvc,
        number: data.number,
        first_last_name: data.name,
        expiration_date: data.expiry.substring(0, 2) + '/' + data.expiry.substring(2),
        accountId: account.data.id,
      })
      .then(() => {
        enqueueSnackbar('Cartão adicionado com sucesso', { variant: 'success' });
        handleClose();
      })
      .catch((err) => {
        const error = err as AxiosError<ApiError>;
        enqueueSnackbar(error.response?.data.message ?? error.message, { variant: 'error' });
      });
  });

  const handleFocus = (e: React.FocusEvent<HTMLInputElement>) => {
    setFocusedInput(e.target.name as keyof FormValues);
  };

  return (
    <Dialog open={visible} onClose={handleClose} maxWidth={'lg'} fullWidth>
      <IconButton
        aria-label="close"
        onClick={() => {
          setVisible(false);
        }}
        sx={{
          position: 'absolute',
          right: 8,
          top: 8,
          zIndex: 1000,
          color: (theme) => theme.palette.grey[500],
        }}
      >
        <Close />
      </IconButton>
      <DialogContent sx={{ mt: 3 }}>
        <Cards
          focused={focusedInput}
          cvc={formState.cvc}
          expiry={formState.expiry}
          name={formState.name}
          number={formState.number}
        />
        <Stack direction="row" spacing={2} sx={{ mt: 2 }}>
          <Controller
            control={control}
            name="number"
            defaultValue=""
            render={({ field }) => (
              <TextField
                {...field}
                onFocus={handleFocus}
                error={!!errors.number}
                helperText={errors.number?.message}
                fullWidth
                variant="standard"
                label="Número do cartão"
                inputProps={{ maxLength: 16 }}
              />
            )}
          />
          <Controller
            control={control}
            name="expiry"
            defaultValue=""
            render={({ field }) => (
              <TextField
                {...field}
                onFocus={handleFocus}
                error={!!errors.expiry}
                helperText={errors.expiry?.message ?? 'MM/AA'}
                fullWidth
                variant="standard"
                label="Data de validade"
                inputProps={{ maxLength: 4 }}
              />
            )}
          />
        </Stack>
        <Stack direction="row" spacing={2} sx={{ mt: 2 }}>
          <Controller
            control={control}
            name="name"
            defaultValue=""
            render={({ field }) => (
              <TextField
                {...field}
                onFocus={handleFocus}
                error={!!errors.name}
                helperText={errors.name?.message}
                fullWidth
                variant="standard"
                label="Nome e sobrenome"
              />
            )}
          />
          <Controller
            control={control}
            name="cvc"
            defaultValue=""
            render={({ field }) => (
              <TextField
                {...field}
                onFocus={handleFocus}
                error={!!errors.cvc}
                helperText={errors.cvc?.message}
                fullWidth
                variant="standard"
                label="Código de segurança"
                inputProps={{ maxLength: 3 }}
              />
            )}
          />
        </Stack>
      </DialogContent>
      <DialogActions>
        <Button color="error" onClick={handleClose} disabled={createCard.isLoading}>
          cancelar
        </Button>
        <LoadingButton loading={createCard.isLoading} variant="contained" onClick={onSubmit} autoFocus>
          Salvar
        </LoadingButton>
      </DialogActions>
    </Dialog>
  );
};

export default forwardRef(AddCardModal);
