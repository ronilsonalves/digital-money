import React from 'react';
import { ListItemButton, ListItemText } from '@mui/material';
import { formatCurrency, getNiceDate } from '../utils/parser';
import { isMoneyIn } from '../utils/helpers';
import { type Activity } from '../types/accounts';
import Link from './Link';
import { routes } from '../config/AppConfig';

interface ActivityListItemProps {
  activity: Activity;
}

export const ActivityListItem = ({ activity }: ActivityListItemProps) => {
  return (
    <ListItemButton LinkComponent={Link} href={`${routes.activities}/${activity.uuid}`}>
      <ListItemText primary={activity.transactionType} sx={{ flex: '0 0 150px' }} />
      <ListItemText primary={activity.description} />
      <ListItemText
        primary={formatCurrency(activity.transactionAmount)}
        secondary={getNiceDate(activity.transactionDate)}
        sx={{ textAlign: 'right', flex: '0 0 150px' }}
        primaryTypographyProps={{
          color: isMoneyIn(activity) ? 'success.main' : 'error.main',
        }}
      />
    </ListItemButton>
  );
};
