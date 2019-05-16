import React, { Component } from "react";
import { SafeAreaView, KeyboardAvoidingView } from "react-native";
import { createAppContainer, createSwitchNavigator } from "react-navigation";

import Input from "../components/Input";
import Card from "../components/Card";
import CardSection from "../components/CardSection";
import Button from "../components/Button";
import MainTabNavigator from "../navigation/MainTabNavigator";

class LoginScreen extends Component {
  state = { email: "", password: "" };

  _onButtonPress = () => {
    this.props.navigation.navigate("Main");
  };

  render() {
    return (
      <KeyboardAvoidingView style={{ flex: 1 }} behavior="padding" enabled>
        <SafeAreaView style={styles.container}>
          <Card>
            <CardSection>
              <Input
                placeholder="example@email.com"
                label="Email"
                value={this.state.email}
                onChangeText={email => this.setState({ email })}
              />
            </CardSection>
            <CardSection>
              <Input
                secureTextEntry
                placeholder="password"
                label="Password"
                value={this.state.password}
                onChangeText={password => this.setState({ password })}
              />
            </CardSection>
            <CardSection>
              <Button
                margin={10}
                onPress={() => this.props.navigation.navigate("Main")}
              >
                Log In
              </Button>
            </CardSection>
          </Card>
        </SafeAreaView>
      </KeyboardAvoidingView>
    );
  }
}

const styles = {
  container: {
    flex: 1,
    flexDirection: "column-reverse"
  }
};

export default createAppContainer(
  createSwitchNavigator(
    {
      // You could add another route here for authentication.
      // Read more at https://reactnavigation.org/docs/en/auth-flow.html
      Login: LoginScreen,
      Main: MainTabNavigator
    },
    {
      initialRouteName: "Login"
    }
  )
);
