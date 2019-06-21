import React from 'react';
import { StyleSheet, Text, View, Image } from 'react-native';
import Onboarding from 'react-native-onboarding-swiper';

export default function App() {
  return (
    <Onboarding
    pages={[
      {
        backgroundColor: '#0091ea',
        image: <Image source={require('./assets/user-random.png')} />,
        title: 'Random User',
        subtitle: 'Explore random user anytime you want',
      },
      {
        backgroundColor: '#0091ea',
        image: <Image source={require('./assets/user-edit.png')} />,
        title: 'Edit User',
        subtitle: 'Edit specific user and save it to firebase',
      },
      {
        backgroundColor: '#0091ea',
        image: <Image source={require('./assets/notification.png')} />,
        title: 'Push Notification',
        subtitle: "Feeling lonely? relax there is notification for you",
      },
    ]}
  />
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
