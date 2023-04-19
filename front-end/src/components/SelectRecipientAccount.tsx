import React, { useState } from 'react';
import { Button, Paper, Stack, TextField, Typography } from '@mui/material';

interface SelectRecipientAccountProps {
  value: string;
  onChange: (value: string) => void;
}

export const SelectRecipientAccount = ({ value: valueFromProps, onChange }: SelectRecipientAccountProps) => {
  const [value, setValue] = useState(valueFromProps);
  return (
    <Paper
      sx={{
        bgcolor: 'background.default',
        px: 5,
        py: 4,
      }}
    >
      <Typography variant="h6" fontWeight={'bold'} color="primary" mb={2}>
        Nova Conta
      </Typography>
      <TextField
        placeholder="Número da conta do destinatário"
        fullWidth
        variant="filled"
        value={value}
        onChange={(e) => {
          setValue(e.target.value);
        }}
      />
      <Stack direction={'row'} justifyContent={'flex-end'} mt={2}>
        <Button
          variant="contained"
          onClick={() => {
            onChange(value);
          }}
        >
          Continuar
        </Button>
      </Stack>
    </Paper>
  );
};
