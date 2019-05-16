import React from 'react';
import { createStackNavigator, createBottomTabNavigator } from 'react-navigation';

import TabBarIcon from '../components/TabBarIcon';
import HomeScreen from '../screens/HomeScreen';
import TopTracksScreen from '../screens/TopTracksScreen';
import InfoScreen from '../screens/InfoScreen';

const HomeStack = createStackNavigator({
  Home: HomeScreen,
});

HomeStack.navigationOptions = {
  tabBarLabel: 'Albums',
  tabBarIcon: ({ focused }) => (
    <TabBarIcon
      focused={focused}
      name={ 'md-albums' }
    />
  ),
};

const TracksStack = createStackNavigator({
  TopTracks: TopTracksScreen,
});

TracksStack.navigationOptions = {
  tabBarLabel: 'Top Tracks',
  tabBarIcon: ({ focused }) => (
    <TabBarIcon
      focused={focused}
      name={ 'md-musical-note' }
    />
  ),
};

const InfoStack = createStackNavigator({
  Info: InfoScreen,
});

InfoStack.navigationOptions = {
  tabBarLabel: 'Info',
  tabBarIcon: ({ focused }) => (
    <TabBarIcon
      focused={focused}
      name={ 'ios-information-circle' }
    />
  ),
};

export default createBottomTabNavigator({
  HomeStack,
  TracksStack,
  InfoStack,
});
