import { EventEmitter, EventSubscription } from 'expo-modules-core';

import { SessionType, SessionWidgetModuleEvents } from './SessionWidget.types';
import SessionWidgetModule from './SessionWidgetModule';

export type SessionWidgetState = {
  consumeActive: boolean;
  createActive: boolean;
  consumeSeconds: number;
  createSeconds: number;
};

export function setSessionState(
  consumeActive: boolean,
  createActive: boolean,
  consumeSeconds: number = 0,
  createSeconds: number = 0
): void {
  return SessionWidgetModule.setSessionState(
    consumeActive,
    createActive,
    consumeSeconds,
    createSeconds
  );
}

export function getSessionState(): SessionWidgetState {
  return SessionWidgetModule.getSessionState();
}

const emitter = new EventEmitter<SessionWidgetModuleEvents>(SessionWidgetModule ?? null);

export function addSessionToggledListener(
  listener: (event: { type: SessionType; isActive: boolean }) => void
): EventSubscription {
  return emitter.addListener('onSessionToggled', listener);
}
