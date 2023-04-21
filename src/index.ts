import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';

import ExpoIntentReceiverModule from './ExpoIntentReceiverModule';
import type { IntentReceiverPayload, IntentInfo } from './ExpoIntentReceiver.types';

const INTENT_RECEIVED_EVENT_NAME = "ExpoIntentReceiver.onNewIntent";
const emitter = new EventEmitter(ExpoIntentReceiverModule ?? NativeModulesProxy.ExpoIntentReceiver);

/**
 * Adds a listener for new intents.
 *
 * @param listener - The listener function to add.
 * @returns A subscription object that can be used to remove the listener later.
 */
export function addChangeListener(listener: (event: IntentReceiverPayload) => void): Subscription {
  return emitter.addListener<IntentReceiverPayload>(INTENT_RECEIVED_EVENT_NAME, listener);
}

/**
 * Gets the initial intent that launched the app.
 *
 * @returns An array of intent info objects, or null if no intent was found.
 */
export function getInitialIntent(): IntentInfo[] | null {
  return ExpoIntentReceiverModule.getInitialIntent?.();
}

/**
 * Clears the intent data for the current activity.
 */
export function clearIntent(): void {
  ExpoIntentReceiverModule.clearIntent?.();
}

export type { IntentReceiverPayload, IntentInfo };
export default {
  addChangeListener,
  getInitialIntent,
  clearIntent,
};