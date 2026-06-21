import { EventEmitter } from 'expo-modules-core';
import SessionWidgetModule from './SessionWidgetModule';
export function setSessionState(consumeActive, createActive, consumeSeconds = 0, createSeconds = 0) {
    return SessionWidgetModule.setSessionState(consumeActive, createActive, consumeSeconds, createSeconds);
}
export function getSessionState() {
    return SessionWidgetModule.getSessionState();
}
const emitter = new EventEmitter(SessionWidgetModule ?? null);
export function addSessionToggledListener(listener) {
    return emitter.addListener('onSessionToggled', listener);
}
//# sourceMappingURL=index.js.map