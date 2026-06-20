import { NativeModule, requireNativeModule } from 'expo';

import { SessionWidgetModuleEvents } from './SessionWidget.types';

declare class SessionWidgetModule extends NativeModule<SessionWidgetModuleEvents> {
  setSessionState(consumeActive: boolean, createActive: boolean): void;
  getSessionState(): { consumeActive: boolean; createActive: boolean };
}

export default requireNativeModule<SessionWidgetModule>('SessionWidget');
