import { EventEmitter, EventSubscription } from 'expo-modules-core';
import SessionWidgetModule from './SessionWidgetModule';
import { SessionType, SessionWidgetModuleEvents } from './SessionWidget.types';

export function setSessionState(consumeActive: boolean, createActive: boolean): void {
  return SessionWidgetModule.setSessionState(consumeActive, createActive);
}

export function getSessionState(): { consumeActive: boolean; createActive: boolean } {
  return SessionWidgetModule.getSessionState();
}

const emitter = new EventEmitter<SessionWidgetModuleEvents>(SessionWidgetModule ?? null);

export function addSessionToggledListener(listener: (event: { type: SessionType; isActive: boolean }) => void): EventSubscription {
  return emitter.addListener('onSessionToggled', listener);
}
