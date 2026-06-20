import { EventEmitter, EventSubscription } from 'expo-modules-core';
import SessionWidgetModule from './SessionWidgetModule';
import { SessionWidgetModuleEvents } from './SessionWidget.types';

export function setSessionState(isActive: boolean): void {
  return SessionWidgetModule.setSessionState(isActive);
}

export function getSessionState(): boolean {
  return SessionWidgetModule.getSessionState();
}

const emitter = new EventEmitter<SessionWidgetModuleEvents>(SessionWidgetModule ?? null);

export function addSessionToggledListener(listener: (event: { isActive: boolean }) => void): EventSubscription {
  return emitter.addListener('onSessionToggled', listener);
}
