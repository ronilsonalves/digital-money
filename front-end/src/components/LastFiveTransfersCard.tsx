import React from 'react';
import { Paper, Typography, Divider, List, ListItemButton, ListItemText, ListItem, Skeleton } from '@mui/material';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';

import { useAccount, useLastFiveTransfers } from '../../src/services/Account';
interface LastFiveTransfersCardProps {
  onClick: (accountId: string) => void;
}

export const LastFiveTransfersCard = ({ onClick }: LastFiveTransfersCardProps) => {
  const account = useAccount();
  const transfers = useLastFiveTransfers(account.data?.id);
  return (
    <Paper elevation={3}>
      <Typography p={2} variant="h6">
        Últimas contas
      </Typography>
      <Divider />
      <List>
        {transfers.isLoading &&
          Array.from(new Array(5)).map((_, index) => (
            <ListItem key={index} sx={{ py: 0 }}>
              <Skeleton animation="wave" width={'100%'} height={50} />
            </ListItem>
          ))}
        {transfers.data?.map((account) => (
          <ListItemButton
            key={account.recipientAccountNumber}
            onClick={() => {
              onClick(account.recipientAccountNumber);
            }}
          >
            <ListItemText>{account.accountOwner}</ListItemText>
            <ArrowForwardIcon />
          </ListItemButton>
        ))}
      </List>
    </Paper>
  );
};
