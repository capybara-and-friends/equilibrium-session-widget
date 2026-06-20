import { NativeModule, requireNativeModule } from 'expo';

import { SessionWidgetModuleEvents } from './SessionWidget.types';

declare class SessionWidgetModule extends NativeModule<SessionWidgetModuleEvents> {
  setSessionState(isActive: boolean): void;
  getSessionState(): boolean;
}

export default requireNativeModule<SessionWidgetModule>('SessionWidget');
