import React, { useRef } from 'react';
import {
  Card,
  CardContent,
  Typography,
  Button,
  Stack,
  ListItem,
  Divider,
  List,
  ListItemText,
  CardActions,
  Radio,
  CardHeader,
} from '@mui/material';
import ControlPointIcon from '@mui/icons-material/ControlPoint';
import AddCardModal, { type ModalHandlesRef } from '../components/AddCardModal';
import { useAccount, useCards } from '../services/Account';
import { type Card as CardType } from '../types/accounts';

interface SelectCardOptionProps {
  onSelectCard: (card: CardType) => void;
  selected?: CardType;
}
export const SelectCardOption = ({ selected, onSelectCard }: SelectCardOptionProps) => {
  const modalRef = useRef<ModalHandlesRef>(null);
  const account = useAccount();
  const cards = useCards(account.data?.id);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const selectedCard = cards.data?.find((card) => card.id === event.target.value);
    selectedCard && onSelectCard(selectedCard);
  };

  return (
    <Card variant="outlined">
      <CardHeader
        title={
          <Typography variant={'h5'} fontWeight={700}>
            Seus cartões
          </Typography>
        }
      />
      <CardContent>
        {cards.isError && <Typography color="error">Erro ao carregar cartões</Typography>}
        {cards.isLoading && <Typography>Carregando...</Typography>}
        <List component={Stack} divider={<Divider />}>
          {cards.isSuccess &&
            cards.data?.map((card) => (
              <ListItem
                key={card.id}
                secondaryAction={
                  <Radio
                    checked={selected?.id === card.id}
                    onChange={handleChange}
                    value={card.id}
                    name="radio-buttons"
                  />
                }
              >
                <ListItemText>{`Final ${card.number.toString().slice(-4)}`}</ListItemText>
              </ListItem>
            ))}
        </List>
      </CardContent>
      <AddCardModal ref={modalRef} />
      <CardActions>
        <Button
          onClick={() => modalRef.current?.openModal()}
          startIcon={<ControlPointIcon fontSize="inherit" />}
          variant="text"
          color="primary"
        >
          Novo cartão
        </Button>
      </CardActions>
    </Card>
  );
};
