import { requireNativeModule } from 'expo';
import { Platform } from 'react-native';
let SessionWidgetModuleInstance;
if (Platform.OS === 'android') {
    try {
        SessionWidgetModuleInstance = requireNativeModule('SessionWidget');
    }
    catch (e) {
        console.error('SessionWidget native module is not available:', e);
    }
}
if (!SessionWidgetModuleInstance) {
    // Return a mock object to prevent crash on non-Android platforms (like iOS)
    SessionWidgetModuleInstance = {
        setSessionState: () => { },
        getSessionState: () => ({
            consumeActive: false,
            createActive: false,
            consumeSeconds: 0,
            createSeconds: 0,
        }),
        addListener: () => { },
        removeListeners: () => { },
    };
}
export default SessionWidgetModuleInstance;
//# sourceMappingURL=SessionWidgetModule.js.map