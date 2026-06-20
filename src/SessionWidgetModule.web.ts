import { registerWebModule, NativeModule } from 'expo';

import { SessionWidgetModuleEvents } from './SessionWidget.types';

// SessionWidgetModule is not available on the web platform.
class SessionWidgetModule extends NativeModule<SessionWidgetModuleEvents> {}

export default registerWebModule(SessionWidgetModule, 'SessionWidgetModule');
