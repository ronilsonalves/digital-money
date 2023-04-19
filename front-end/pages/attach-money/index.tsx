import React from 'react';
import { Paper, Typography, Button } from '@mui/material';
import Link from '../../src/components/Link';
import { routes } from '../../src/config/AppConfig';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';
import CreditCardOutlinedIcon from '@mui/icons-material/CreditCardOutlined';
import { AccountCard } from '../../src/components/AccountCard';

const AttachMoney = () => {
  return (
    <>
      <AccountCard />
      <Paper elevation={3} sx={{ mt: 3 }}>
        <Button
          LinkComponent={Link}
          href={routes.attachMoney.steps}
          startIcon={<CreditCardOutlinedIcon sx={{ fontSize: '30px !important' }} />}
          fullWidth
          sx={{ p: 5 }}
        >
          <Typography variant="h6" fontWeight={700}>
            Selecionar cartão
          </Typography>
          <ArrowForwardIcon sx={{ ml: 'auto !important' }} />
        </Button>
      </Paper>
    </>
  );
};

export default AttachMoney;
