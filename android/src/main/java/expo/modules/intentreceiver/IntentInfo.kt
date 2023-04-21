package expo.modules.intentreceiver

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

class IntentInfo(
    @Field val contentUri: String? = null,
    @Field val extension: String? = null,
    @Field val fileName: String? = null,
    @Field val mimeType: String? = null,
) : Record