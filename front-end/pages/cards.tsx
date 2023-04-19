import React, { useRef } from 'react';
import { type NextPage } from 'next';
import { Card, CardContent, Typography, Button, Stack, ListItem, Divider, List, ListItemText } from '@mui/material';
import LoadingButton from '@mui/lab/LoadingButton';
import ControlPointIcon from '@mui/icons-material/ControlPoint';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';
import AddCardModal, { type ModalHandlesRef } from '../src/components/AddCardModal';
import { useAccount, useCards, useDeleteCard } from '../src/services/Account';

const Cards: NextPage = () => {
  const modalRef = useRef<ModalHandlesRef>(null);
  const account = useAccount();
  const cards = useCards(account.data?.id);
  const deleteCard = useDeleteCard();

  const handleDeleteCard = (id: string) => {
    if (!account.data?.id) return;

    deleteCard.mutate({ accountId: account.data.id, cardId: id });
  };

  return (
    <>
      <Card elevation={3} sx={{ bgcolor: 'background.default' }}>
        <CardContent>
          <Typography fontWeight={700}>Adicione seu cartão de crédito ou débito</Typography>
          <Button
            onClick={() => modalRef.current?.openModal()}
            variant="text"
            color="primary"
            startIcon={<ControlPointIcon fontSize="inherit" />}
            endIcon={<ArrowForwardIcon fontSize="inherit" />}
            size="large"
            fullWidth
            sx={{
              justifyContent: 'flex-start',
              textTransform: 'capitalize',
              fontSize: 20,
              fontWeight: 700,
              mt: 3,
              '.MuiButton-endIcon': {
                ml: 'auto',
                fontSize: 40,
              },
              '.MuiButton-startIcon': {
                fontSize: 40,
                mr: 3,
              },
            }}
          >
            Adicionar cartão
          </Button>
        </CardContent>
      </Card>
      <Card elevation={3} sx={{ mt: 3 }}>
        <CardContent>
          <Typography fontWeight={700}>Seus cartões</Typography>
          {cards.isError && <Typography color="error">Erro ao carregar cartões</Typography>}
          {cards.isLoading && <Typography>Carregando...</Typography>}
          <List component={Stack} divider={<Divider />}>
            {cards.isSuccess &&
              cards.data?.map((card) => (
                <ListItem
                  key={card.id}
                  secondaryAction={
                    <LoadingButton
                      loading={deleteCard.isLoading && deleteCard.variables?.cardId === card.id}
                      onClick={() => {
                        handleDeleteCard(card.id);
                      }}
                    >
                      Remover
                    </LoadingButton>
                  }
                >
                  {/* <ListItemText>{` ${card.first_last_name}`}</ListItemText> */}
                  <ListItemText>{`**** **** **** ${card.number.toString().slice(-4)}`}</ListItemText>
                </ListItem>
              ))}
          </List>
        </CardContent>
      </Card>
      <AddCardModal ref={modalRef} />
    </>
  );
};

export default Cards;
