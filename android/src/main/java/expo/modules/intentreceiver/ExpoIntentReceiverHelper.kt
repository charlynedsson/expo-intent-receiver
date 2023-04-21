package expo.modules.intentreceiver

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import java.io.File

object ExpoIntentReceiverHelper {
  /**
   * Parses the given intent to extract the URIs for the content to be shared.
   * @param intent The intent received by the app.
   * @return The list of URIs to be shared. Null if no valid content is found.
   */
  fun parseIntent(intent: Intent): List<Uri>? {
      // Extract the action and type from the intent.
      val action = intent.action
      val type = intent.type

      // If the intent does not have a valid type or action, return null.
      if ((type?.startsWith("text") != false) || (action != Intent.ACTION_SEND && action != Intent.ACTION_SEND_MULTIPLE)) {
          return null
      }

      // Extract the list of URIs from the intent.
      val uriList = when (action) {
          Intent.ACTION_SEND -> listOfNotNull(intent.getParcelableExtra(Intent.EXTRA_STREAM))
          Intent.ACTION_SEND_MULTIPLE -> intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM)
          else -> null
      }?.filterIsInstance<Uri>() ?: return null

      // Return the list of URIs, or null if it's empty.
      return uriList.ifEmpty { null }
  }

  /**
   * Extracts information about the content to be shared from the given URIs.
   * @param context The context of the app.
   * @param contentUris The list of URIs to extract the information from.
   * @return The list of `IntentInfo` objects with the information about the shared content.
   */
  fun getIntentInfo(context: Context, contentUris: List<Uri>): List<IntentInfo>? {        
      val intentInfoList = mutableListOf<IntentInfo>()
      val contentResolver = context.contentResolver
      val mimeTypeMap = MimeTypeMap.getSingleton()

      // Iterate over the list of URIs to extract information about the content.
      for (uri in contentUris) {
          // Extract the MIME type of the content.
          val mimeType = contentResolver.getType(uri)
          // Query the content provider for information about the content.
          val cursor = contentResolver.query(uri, null, null, null, null) ?: continue
          cursor.use {
              if (!cursor.moveToFirst()) {
                  continue
              }

              // Extract the display name of the content.
              val displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
              // Create a file object from the display name.
              val file = File(displayName)

              // Create an `IntentInfo` object with the extracted information and add it to the list.
              intentInfoList.add(
                  IntentInfo(
                      contentUri = uri.toString(),
                      extension = mimeTypeMap.getExtensionFromMimeType(mimeType),
                      fileName = file.nameWithoutExtension,
                      mimeType = mimeType
                  )
              )
          }
      }
      // Return the list of `IntentInfo` objects, or null if it's empty.
      return intentInfoList.ifEmpty { null }
  }
}