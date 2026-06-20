import * as SessionWidget from 'expo-session-widget';
import { useEvent } from 'expo';
import { Button, SafeAreaView, ScrollView, Text, View } from 'react-native';
import { useEffect, useState } from 'react';

export default function App() {
  const [isActive, setIsActive] = useState(false);

  useEffect(() => {
    setIsActive(SessionWidget.getSessionState());
    
    const sub = SessionWidget.addSessionToggledListener((event) => {
      setIsActive(event.isActive);
    });
    
    return () => sub.remove();
  }, []);

  const toggleSession = () => {
    const newState = !isActive;
    SessionWidget.setSessionState(newState);
    setIsActive(newState);
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView style={styles.container}>
        <Text style={styles.header}>Session Widget Test</Text>
        <Group name="State">
          <Text style={{ fontSize: 20, marginBottom: 20 }}>
            Session is: {isActive ? 'ACTIVE' : 'INACTIVE'}
          </Text>
          <Button title={isActive ? "Stop Session" : "Start Session"} onPress={toggleSession} />
        </Group>
      </ScrollView>
    </SafeAreaView>
  );
}

function Group(props: { name: string; children: React.ReactNode }) {
  return (
    <View style={styles.group}>
      <Text style={styles.groupHeader}>{props.name}</Text>
      {props.children}
    </View>
  );
}

const styles = {
  header: { fontSize: 30, margin: 20 },
  groupHeader: { fontSize: 20, marginBottom: 20 },
  group: { margin: 20, backgroundColor: '#fff', borderRadius: 10, padding: 20 },
  container: { flex: 1, backgroundColor: '#eee' },
};
