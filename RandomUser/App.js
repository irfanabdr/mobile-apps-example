import React from "react";
import { AppLoading } from "expo";
import * as Font from "expo-font";
import OnboardingScreen from "./src/screens/OnboardingScreen";

export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = { initialLoading: true };
  }

  async componentWillMount() {
    await Font.loadAsync({
      Roboto: require("native-base/Fonts/Roboto.ttf"),
      Roboto_medium: require("native-base/Fonts/Roboto_medium.ttf")
    });
    this.setState({ initialLoading: false });
  }

  render() {
    if (this.state.initialLoading) {
      return <AppLoading />;
    }

    return <OnboardingScreen />;
  }
}
