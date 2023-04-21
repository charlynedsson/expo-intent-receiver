const INTENT_RECEIVED_BUNDLE_NAME = "data";

export type IntentInfo = {
  contentUri: string;
  extension: string;
  fileName: string;
  mimeType: string;
}

export type IntentReceiverPayload = {
  [INTENT_RECEIVED_BUNDLE_NAME] : IntentInfo[];
}