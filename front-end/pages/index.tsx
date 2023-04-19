import React from 'react';
import { Stack, Typography, Container, Divider } from '@mui/material';
import Image from 'next/image';

const Home = () => {
  return (
    <Stack
      flex={1}
      sx={{
        position: 'relative',
      }}
    >
      <Image
        src="/images/LandingPage.svg"
        alt="home-background"
        fill
        style={{
          objectFit: 'cover',
          zIndex: -1,
        }}
      />
      <Container
        maxWidth="xl"
        sx={{
          flex: 1,
          display: 'flex',
          flexDirection: 'column',
        }}
      >
        <Typography variant="h1" color={'white'} maxWidth={418} mt={9}>
          De agora em diante, faça mais com seu dinheiro
        </Typography>

        <Typography color={'secondary.main'} maxWidth={418} mt={3} fontSize={34}>
          Sua nova
          <Typography component={'span'} fontSize={34} fontWeight={700}>
            {' carteira virtual'}
          </Typography>
        </Typography>

        <Stack
          sx={{
            flexDirection: 'row',
            flexWrap: 'wrap',
            gap: { xs: 3, md: 6 },
            mt: { xs: 6, md: 9 },
            mb: 3,
            justifyContent: 'space-evenly',
          }}
        >
          <Stack
            gap={1.5}
            sx={{
              bgcolor: 'white',
              borderRadius: 3.75,
              p: 3.75,
              height: 250,
              width: 500,
            }}
          >
            <Typography variant="h4" color={'initial'} fontWeight={'bold'} fontSize={36}>
              Transferência
            </Typography>
            <Divider
              sx={{
                borderColor: 'secondary.main',
                borderWidth: 1.5,
              }}
            />
            <Typography color={'initial'}>
              Com a Digital Money House você pode transferir dinheiro para outras contas, bem como receber
              transferências e centralizar seus investimentos na nossa carteira virtual.
            </Typography>
          </Stack>

          <Stack
            gap={1.5}
            sx={{
              bgcolor: 'white',
              borderRadius: 3.75,
              p: 3.75,
              height: 250,
              width: 500,
            }}
          >
            <Typography variant="h4" color={'initial'} fontWeight={'bold'} fontSize={36}>
              Pagamento de serviços
            </Typography>
            <Divider
              sx={{
                borderColor: 'secondary.main',
                borderWidth: 1.5,
              }}
            />
            <Typography color={'initial'}>
              Pague mensalmente por serviços com apenas 3 clicks. Fácil, rápido e conveniente. Esqueça os boletos em
              papel.
            </Typography>
          </Stack>
        </Stack>
      </Container>
    </Stack>
  );
};

export default Home;
