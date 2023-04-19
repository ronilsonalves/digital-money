import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { useForm } from 'react-hook-form';
import Link from '../src/components/Link';
import * as yup from 'yup';
import { yupResolver } from '@hookform/resolvers/yup';
import { CPFInput } from '../src/components/inputs/CPFInput';
import { PhoneInput } from '../src/components/inputs/PhoneInput';
import { UseSignUp } from '../src/services/auth';
import Router from 'next/router';
import { enqueueSnackbar } from 'notistack';

interface FormValues {
  name: string;
  lastName: string;
  email: string;
  password: string;
  phone: string;
  cpf: string;
}

const schema = yup.object().shape({
  name: yup.string().required('Nome é obrigatório'),
  lastName: yup.string().required('Sobrenome é obrigatório'),
  email: yup.string().required('Email é obrigatório').email('Email inválido'),
  password: yup.string().required('Senha é obrigatória').min(8, 'Senha deve ter no mínimo 6 caracteres'),
  phone: yup
    .string()
    .required('Telefone é obrigatório')
    .matches(/^(\([0-9]{2}\)) ([0-9]{5})-([0-9]{4})$/, 'Telefone deve ter 11 dígitos numéricos'),
  cpf: yup
    .string()
    .required('CPF é obrigatório')
    .matches(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, 'CPF deve ter 11 dígitos numéricos'),
});

const SignUp = () => {
  const signUp = UseSignUp();
  const {
    handleSubmit,
    register,
    formState: { errors },
    watch,
  } = useForm<FormValues>({
    resolver: yupResolver(schema),
    reValidateMode: 'onBlur',
  });

  const onSubmit = (data: any) => {
    signUp.mutate(data, {
      onSuccess: () => {
        console.log('success');
        void Router.push('/sign-in');
      },
      onError: (error) => {
        enqueueSnackbar((error as any).message, { variant: 'error' });
      },
    });
  };

  console.log(watch('phone'));

  return (
    <Container component="main" maxWidth="xs">
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Sign up
        </Typography>
        <Box component="form" noValidate onSubmit={handleSubmit(onSubmit)} sx={{ mt: 3 }}>
          <Grid container spacing={2}>
            <Grid item xs={12} sm={6}>
              <TextField
                {...register('name')}
                autoComplete="given-name"
                required
                fullWidth
                label="Nome"
                autoFocus
                error={!!errors.name}
                helperText={errors.name?.message}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                {...register('lastName')}
                required
                fullWidth
                label="Sobrenome"
                autoComplete="family-name"
                error={!!errors.lastName}
                helperText={errors.lastName?.message}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                {...register('cpf')}
                autoComplete="cpf"
                required
                fullWidth
                label="CPF"
                error={!!errors.cpf}
                helperText={errors.cpf?.message}
                InputProps={{
                  inputComponent: CPFInput as any,
                }}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                {...register('phone')}
                required
                fullWidth
                label="Telefone"
                autoComplete="phone"
                error={!!errors.phone}
                helperText={errors.phone?.message}
                InputProps={{
                  inputComponent: PhoneInput as any,
                }}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                {...register('email')}
                required
                fullWidth
                label="Email"
                autoComplete="email"
                error={!!errors.email}
                helperText={errors.email?.message}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                {...register('password')}
                required
                fullWidth
                label="Senha"
                type="password"
                autoComplete="new-password"
                error={!!errors.password}
                helperText={errors.password?.message}
              />
            </Grid>
          </Grid>
          <Button type="submit" fullWidth variant="contained" sx={{ mt: 3, mb: 2 }}>
            Criar conta
          </Button>
          <Grid container justifyContent="flex-end">
            <Grid item>
              <Link href="sign-in" variant="body2">
                Já tem uma conta? Faça login
              </Link>
            </Grid>
          </Grid>
        </Box>
      </Box>
    </Container>
  );
};

export default SignUp;
