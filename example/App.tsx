import * as SessionWidget from 'expo-session-widget';
import { Button, SafeAreaView, ScrollView, Text, View } from 'react-native';
import { useEffect, useState } from 'react';

export default function App() {
  const [state, setState] = useState<SessionWidget.SessionWidgetState>({
    consumeActive: false,
    createActive: false,
    consumeSeconds: 0,
    createSeconds: 0,
  });

  useEffect(() => {
    setState(SessionWidget.getSessionState());

    const sub = SessionWidget.addSessionToggledListener((event) => {
      setState(SessionWidget.getSessionState());
      console.log('Session widget toggled', event);
    });

    return () => sub.remove();
  }, []);

  const toggleSession = (type: 'consume' | 'create') => {
    const currentState = SessionWidget.getSessionState();
    const nextState = {
      ...currentState,
      consumeActive: type === 'consume' ? !currentState.consumeActive : false,
      createActive: type === 'create' ? !currentState.createActive : false,
    };

    SessionWidget.setSessionState(
      nextState.consumeActive,
      nextState.createActive,
      nextState.consumeSeconds,
      nextState.createSeconds
    );
    setState(SessionWidget.getSessionState());
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView style={styles.container}>
        <Text style={styles.header}>Session Widget Test</Text>
        <Group name="State">
          <Text style={{ fontSize: 20, marginBottom: 20 }}>
            Consume: {state.consumeActive ? 'ACTIVE' : 'INACTIVE'} ({state.consumeSeconds}s)
          </Text>
          <Text style={{ fontSize: 20, marginBottom: 20 }}>
            Create: {state.createActive ? 'ACTIVE' : 'INACTIVE'} ({state.createSeconds}s)
          </Text>
          <Button title="Toggle Consume" onPress={() => toggleSession('consume')} />
          <Button title="Toggle Create" onPress={() => toggleSession('create')} />
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
