// Reexport the native module. On web, it will be resolved to SessionWidgetModule.web.ts
// and on native platforms to SessionWidgetModule.ts
export { default } from './SessionWidgetModule';
export * from './SessionWidget.types';
