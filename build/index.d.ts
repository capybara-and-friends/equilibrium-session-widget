import { EventSubscription } from 'expo-modules-core';
import { SessionType } from './SessionWidget.types';
export type SessionWidgetState = {
    consumeActive: boolean;
    createActive: boolean;
    consumeSeconds: number;
    createSeconds: number;
};
export declare function setSessionState(consumeActive: boolean, createActive: boolean, consumeSeconds?: number, createSeconds?: number): void;
export declare function getSessionState(): SessionWidgetState;
export declare function addSessionToggledListener(listener: (event: {
    type: SessionType;
    isActive: boolean;
}) => void): EventSubscription;
//# sourceMappingURL=index.d.ts.map