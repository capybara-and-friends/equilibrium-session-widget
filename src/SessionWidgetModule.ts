import { NativeModule, requireNativeModule } from 'expo';

import { SessionWidgetModuleEvents } from './SessionWidget.types';

declare class SessionWidgetModule extends NativeModule<SessionWidgetModuleEvents> {
  setSessionState(
    consumeActive: boolean,
    createActive: boolean,
    consumeSeconds: number,
    createSeconds: number
  ): void;
  getSessionState(): {
    consumeActive: boolean;
    createActive: boolean;
    consumeSeconds: number;
    createSeconds: number;
  };
}

export default requireNativeModule<SessionWidgetModule>('SessionWidget');
