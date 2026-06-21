import { registerWebModule, NativeModule } from 'expo';
// SessionWidgetModule is not available on the web platform.
class SessionWidgetModule extends NativeModule {
}
export default registerWebModule(SessionWidgetModule, 'SessionWidgetModule');
//# sourceMappingURL=SessionWidgetModule.web.js.map