import { NativeModule, requireNativeModule } from 'expo';

import { SessionWidgetModuleEvents } from './SessionWidget.types';

declare class SessionWidgetModule extends NativeModule<SessionWidgetModuleEvents> {
  hello(): string;
}

export default requireNativeModule<SessionWidgetModule>('SessionWidget');
