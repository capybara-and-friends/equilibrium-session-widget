import { NativeModule } from 'expo';
import { SessionWidgetModuleEvents } from './SessionWidget.types';
declare class SessionWidgetModule extends NativeModule<SessionWidgetModuleEvents> {
    setSessionState(consumeActive: boolean, createActive: boolean, consumeSeconds: number, createSeconds: number): void;
    getSessionState(): {
        consumeActive: boolean;
        createActive: boolean;
        consumeSeconds: number;
        createSeconds: number;
    };
}
declare const _default: SessionWidgetModule;
export default _default;
//# sourceMappingURL=SessionWidgetModule.d.ts.map