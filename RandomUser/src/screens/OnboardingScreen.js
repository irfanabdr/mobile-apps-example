import React from "react";
import { Image } from "react-native";
import Onboarding from "react-native-onboarding-swiper";
import { createAppContainer, createSwitchNavigator } from "react-navigation";

import MainScreen from "./MainScreen";

class OnboardingScreen extends React.Component {
  render() {
    return (
      <Onboarding
        onDone={this._actionToMain}
        onSkip={this._actionToMain}
        pages={[
          {
            backgroundColor: "#0091ea",
            image: <Image source={require("../../assets/user-random.png")} />,
            title: "Random User",
            subtitle: "Explore random user anytime you want"
          },
          {
            backgroundColor: "#0091ea",
            image: <Image source={require("../../assets/user-edit.png")} />,
            title: "Edit User",
            subtitle: "Edit specific user and save it to firebase"
          },
          {
            backgroundColor: "#0091ea",
            image: <Image source={require("../../assets/notification.png")} />,
            title: "Push Notification",
            subtitle: "Feeling lonely? relax there is notification for you"
          }
        ]}
      />
    );
  }

  _actionToMain = () => {
    this.props.navigation.navigate("Main");
  };
}

export default createAppContainer(
  createSwitchNavigator(
    {
      Onboardings: OnboardingScreen,
      Main: MainScreen
    },
    {
      initialRouteName: "Onboardings"
    }
  )
);
