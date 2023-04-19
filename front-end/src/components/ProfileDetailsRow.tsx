import { Stack, Typography } from '@mui/material';
interface ProfileDetailsRowProps {
  label: string;
  value: string;
}
export const ProfileDetailsRow = ({ label, value }: ProfileDetailsRowProps) => {
  return (
    <Stack direction="row" gap={1} flexWrap={'wrap'}>
      <Stack
        sx={{
          flexBasis: { xs: '100%', sm: '30%' },
        }}
      >
        <Typography variant="body1">{label}</Typography>
      </Stack>
      <Stack flexGrow={1}>
        <Typography variant="body1" color={'text.disabled'} fontWeight={'bold'} sx={{ ml: 1 }}>
          {value}
        </Typography>
      </Stack>
    </Stack>
  );
};
