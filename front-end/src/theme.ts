import { Open_Sans } from 'next/font/google';
import { createTheme } from '@mui/material/styles';
import { red } from '@mui/material/colors';

export const openSans = Open_Sans({
  weight: ['300', '400', '500', '700'],
  subsets: ['latin'],
  display: 'swap',
  fallback: ['Helvetica', 'Arial', 'sans-serif'],
});

// Create a theme instance.
const theme = createTheme({
  palette: {
    mode: 'dark',
    primary: {
      main: '#0AEB8C',
    },
    secondary: {
      main: '#D2FFEC',
    },
    error: {
      main: red.A400,
    },
    background: {
      default: '#33373e',
      paper: '#052A2D',
    },
    common: {
      black: '#1D1D1D',
      white: '#fff',
    },
    contrastThreshold: 3,
    text: {
      primary: '#fff',
      secondary: '#B2BAC2',
      disabled: 'rgba(255, 255, 255, 0.5)',
    },
  },
  typography: {
    fontFamily: openSans.style.fontFamily,
    fontSize: 16,
    h1: {
      fontSize: 48,
      fontWeight: 400,
      lineHeight: 1.1142857142857143,
      scrollMarginTop: 'calc(var(--MuiDocs-header-height) + 72px)',
    },
    h2: {
      fontSize: 'clamp(1.5rem, 0.9643rem + 1.4286vw, 2.25rem)',
      fontWeight: 800,
      lineHeight: 1.2222222222222223,
      color: '#E7EBF0',
      scrollMarginTop: 'calc(var(--MuiDocs-header-height) + 72px)',
    },
  },
  components: {
    MuiButton: {
      defaultProps: {
        disableElevation: true,
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          backgroundImage: 'none',
        },
      },
    },
  },
});

export default theme;
