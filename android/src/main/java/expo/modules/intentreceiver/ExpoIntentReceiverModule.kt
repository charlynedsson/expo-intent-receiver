package expo.modules.intentreceiver

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

// Name of the event that will be emitted when a new intent is received
private const val INTENT_RECEIVED_EVENT_NAME = "ExpoIntentReceiver.onNewIntent"

// Name of the data bundle that will be sent with the INTENT_RECEIVED_EVENT_NAME event
private const val INTENT_RECEIVED_BUNDLE_NAME = "data"

class ExpoIntentReceiverModule : Module() {

  override fun definition() = ModuleDefinition {

    Name("ExpoIntentReceiver")

    // Register the INTENT_RECEIVED_EVENT_NAME event with the event emitter
    Events(INTENT_RECEIVED_EVENT_NAME)

    // Handle new intents that are received by the activity
    OnNewIntent { intent ->
      try {
        handleIntent(intent)?.let { intentInfo ->
          sendIntent(intentInfo)
        }
      } catch (e: Exception) {
        throw RuntimeException(e)
      }
    }

    // Retrieve the initial intent that was used to launch the activity
    Function("getInitialIntent") {
      appContext.currentActivity?.let { activity ->
        handleIntent(activity.intent)?.let { intentInfo ->
          return@Function intentInfo
        }
      }
      return@Function null
    }

    // Clear the current intent of the activity
    Function("clearIntent") {
      clearIntent()
    }
  }

  // Extracts intent information from the provided intent
  private fun handleIntent(intent: Intent): List<IntentInfo>? {
    // Only handle intents that contain a file or a stream
    if (intent.type?.startsWith("text") != false ||
        intent.action in listOf(Intent.ACTION_SEND, Intent.ACTION_SEND_MULTIPLE)
    ) {
        return null
    }
    
    // Parse the intent to get the list of URIs for the files or streams
    val parsedIntents = ExpoIntentReceiverHelper.parseIntent(intent)
    
    // Retrieve the context of the activity
    val context = appContext.reactContext ?: return null
    
    // Extract intent information from the URIs
    return ExpoIntentReceiverHelper.getIntentInfo(context, parsedIntents)
  }

  // Emits the INTENT_RECEIVED_EVENT_NAME event with the intent information
  private fun sendIntent(intentInfo: List<IntentInfo>) {
    sendEvent(INTENT_RECEIVED_EVENT_NAME, mapOf(
      INTENT_RECEIVED_BUNDLE_NAME to intentInfo
    ))
  }

  // Clears the current intent of the activity
  private fun clearIntent() {
    appContext.currentActivity?.apply {
        val intent = Intent().apply {
            replaceExtras(Bundle())
            action = ""
            data = null
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
  }
}