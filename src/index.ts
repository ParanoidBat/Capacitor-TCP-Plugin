import { registerPlugin } from '@capacitor/core';

import type { TCPPlugin } from './definitions';

const TCP = registerPlugin<TCPPlugin>('TCP', {
  web: () => import('./web').then(m => new m.TCPWeb()),
});

export * from './definitions';
export { TCP };
