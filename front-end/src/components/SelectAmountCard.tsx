import { Card, CardContent, Typography, TextField, InputAdornment, CardActions, Button } from '@mui/material';
import { MoneyInput } from './inputs/MoneyInput';

type SelectCardOptionProps = {
  title: string;
  onValueChange: (value: string) => void;
  value: string;
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
export const SelectAmountCard = ({ title, value, onValueChange, onContinue, onBack }: SelectCardOptionProps) => {
  const handleValueChange = (value: string) => {
    onValueChange(value);
  };

  return (
    <Card variant="outlined">
      <CardContent>
        <Typography fontWeight={700}>{title}</Typography>
        <TextField
          sx={{ mt: 4 }}
          fullWidth
          value={value}
          onChange={(e) => {
            handleValueChange(e.target.value);
          }}
          autoFocus
          InputProps={{
            inputComponent: MoneyInput as any,
            startAdornment: <InputAdornment position="start">R$</InputAdornment>,
          }}
        />
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
