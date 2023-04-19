import React from 'react';
import { Button, Card, CardActions, CardContent, Typography } from '@mui/material';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';

type SelectDateCardProps = {
  title: string;
  value: Date | null;
  onValueChange: (value: Date | null) => void;
} & (
  | {
      onContinue?: never;
      onBack?: never;
    }
  | {
      onContinue: () => void;
      onBack: () => void;
    }
);
export const SelectDateCard = ({ value, onValueChange, title, onBack, onContinue }: SelectDateCardProps) => {
  return (
    <Card variant="outlined">
      <CardContent>
        <Typography fontWeight={700} mb={2}>
          {title}
        </Typography>
        <LocalizationProvider dateAdapter={AdapterDateFns}>
          <DatePicker
            value={value}
            onChange={(newValue) => {
              onValueChange(newValue);
            }}
          />
        </LocalizationProvider>
      </CardContent>
      {onContinue && (
        <CardActions>
          <Button variant="contained" onClick={onBack}>
            Voltar
          </Button>
          <Button variant="contained" onClick={onContinue} sx={{ ml: 'auto !important' }}>
            Continuar
          </Button>
        </CardActions>
      )}
    </Card>
  );
};
