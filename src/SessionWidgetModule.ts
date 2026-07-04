import { NativeModule, requireNativeModule } from 'expo';
import { Platform } from 'react-native';

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

let SessionWidgetModuleInstance: any;

if (Platform.OS === 'android') {
  try {
    SessionWidgetModuleInstance = requireNativeModule<SessionWidgetModule>('SessionWidget');
  } catch (e) {
    console.error('SessionWidget native module is not available:', e);
  }
}

if (!SessionWidgetModuleInstance) {
  // Return a mock object to prevent crash on non-Android platforms (like iOS)
  SessionWidgetModuleInstance = {
    setSessionState: () => {},
    getSessionState: () => ({
      consumeActive: false,
      createActive: false,
      consumeSeconds: 0,
      createSeconds: 0,
    }),
    addListener: () => {},
    removeListeners: () => {},
  };
}

export default SessionWidgetModuleInstance;
