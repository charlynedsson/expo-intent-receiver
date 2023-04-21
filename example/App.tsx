import { ScrollView, StyleSheet, Text } from 'react-native';

import * as ExpoIntentReceiver from 'expo-intent-receiver';
import React from 'react';

export default function App() {
  const [data, setData] = React.useState<ExpoIntentReceiver.IntentInfo[]>([]);
  const refIntent = React.useRef(ExpoIntentReceiver.getInitialIntent());

  React.useEffect(() => {
    const subscription = ExpoIntentReceiver.addIntentListener(({ data }) => {
      setData((currentData) => [...currentData, ...data])
    })
    return () => subscription.remove();
  }, []);

  return (
    <ScrollView style={styles.container}>
      <Text>Initial intent: {JSON.stringify(refIntent)}</Text>
      {
        data.map((d, i) => <Text key={`item-${i}`} style={styles.line}>{i} - {JSON.stringify(d)}</Text>)
      }
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    padding: 16,
  },
  line: {
    paddingVertical: 8,
  }
});
