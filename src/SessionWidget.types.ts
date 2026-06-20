export type SessionType = 'consume' | 'create';

export type SessionWidgetModuleEvents = {
  onSessionToggled: (event: { type: SessionType; isActive: boolean }) => void;
};
