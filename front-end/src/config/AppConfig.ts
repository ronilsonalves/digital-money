export const drawerWidth = 240;

export const routes = {
  home: '/',
  login: '/sign-in',
  register: '/sign-up',
  forgotPassword: '/forgot-password',
  dashboard: '/dashboard',
  activities: '/activities',
  profile: '/profile',
  attachMoney: {
    index: '/attach-money',
    steps: '/attach-money/steps',
    success: '/attach-money/success',
  },
  send: '/send',
  cards: '/cards',
};

export const drawerList = [
  {
    name: 'Início',
    icon: 'home',
    path: routes.dashboard,
  },
  {
    name: 'Atividades',
    icon: 'assignment',
    path: routes.activities,
  },
  {
    name: 'Seu perfil',
    icon: 'person',
    path: routes.profile,
  },
  {
    name: 'Carregar valor',
    icon: 'attach_money',
    path: routes.attachMoney.index,
  },
  {
    name: 'Enviar valor',
    icon: 'payments',
    path: routes.send,
  },
  {
    name: 'Cartões',
    icon: 'credit_card',
    path: routes.cards,
  },
];
