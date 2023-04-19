import React from 'react';
import { IconButton, type IconButtonProps } from '@mui/material';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import { enqueueSnackbar } from 'notistack';

interface ClipboardButtonProps extends Omit<IconButtonProps, 'onClick'> {
  value: string;
}

export const ClipboardButton = ({ value, ...props }: ClipboardButtonProps) => {
  const handleCopyValue = async () => {
    await navigator.clipboard.writeText(value);
    enqueueSnackbar('Texto copiado!', {
      variant: 'success',
      anchorOrigin: { vertical: 'top', horizontal: 'center' },
      autoHideDuration: 500,
    });
  };

  return (
    // eslint-disable-next-line @typescript-eslint/no-misused-promises
    <IconButton {...props} onClick={handleCopyValue}>
      <ContentCopyIcon />
    </IconButton>
  );
};
