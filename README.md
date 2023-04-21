# expo-intent-receiver

Expo module to receive Android intents. **Supports only expo Android**.

[![npm version](https://badge.fury.io/js/expo-intent-receiver.svg)](https://badge.fury.io/js/expo-intent-receiver) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![React-Native](https://img.shields.io/badge/-React%20Native-grey?style=flat&logo=react)](https://reactnative.dev/) [![Expo](https://img.shields.io/badge/Expo-4630EB.svg?style=flat-square&logo=EXPO&labelColor=f3f3f3&logoColor=000)](https://expo.dev/client)

# Installation

### Add the package to your npm dependencies

```
npm install expo-intent-receiver
```

### Configure Android Intent Filters in app.json

```json
{
    "android": {      
        "intentFilters": [
            {
                "action": "SEND_MULTIPLE",
                "category": [
                    "DEFAULT"
                ],
                "data": [
                    {
                    "mimeType": "image/*"
                    }
                ]
            },
            {
                "action": "SEND",
                "category": [
                    "DEFAULT"
                ],
                "data": [
                    {
                    "mimeType": "video/*"
                    }
                ]
            }        
        ]
    }
}
```

### Usage

```js
import React from 'react';
import { View, Text } from 'react-native';
import * as ExpoIntentReceiver from 'expo-intent-receiver';

export default function App() {
  const refIntent = React.useRef(ExpoIntentReceiver.getInitialIntent());
  const [data, setData] = React.useState<ExpoIntentReceiver.IntentInfo[]>([]);
  
  React.useEffect(() => {
    const subscription = ExpoIntentReceiver.addChangeListener(({ data }) => {
      setData((currentData) => [...currentData, ...data])
    })
    return () => subscription.remove();
  }, []);

  return (
    <View>
      <Text>Initial intent: {JSON.stringify(refIntent)}</Text>
      {
        data.map((d, i) => 
            <Text 
                key={`item-${i}`}>
                    {i} - {JSON.stringify(d)}
            </Text>
        )
      }
    </View>
  );
}

```

# Module

| Method | Return | Description |
|------|------|-------------|
|addIntentListener | `IntentReceiverPayload` | Will fire when new filtered intent is received and app is in the background.|
|getInitialIntent | `IntentInfo[]` | Will return `ÌntentInfo[]` if filtered intent is available. To be used to capture filtered intent that launched the app.|
|clearIntent | `void` | Clear intent.|

# Types
```ts
type IntentInfo = {
  contentUri: string;
  extension: string;
  fileName: string;
  mimeType: string;
}

type IntentReceiverPayload = {
  data : IntentInfo[];
}
```

# Contributing

Contributions are very welcome!
