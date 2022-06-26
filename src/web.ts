import { WebPlugin } from '@capacitor/core';

import type { TCPPlugin } from './definitions';

export class TCPWeb extends WebPlugin implements TCPPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
