import React from "react";
import { Notifications } from "expo";
import Constants from "expo-constants";
import * as Permissions from "expo-permissions";
import { StyleSheet, Image, View, Alert, Platform } from "react-native";
import {
  Container,
  Content,
  Form,
  Item,
  Input,
  Label,
  Button,
  Text,
  Root,
  Toast
} from "native-base";

import { firebaseApp } from "../../FirebaseConfig";
import { Spinner } from "../components/Spinner";

export default class EditScreen extends React.Component {
  state = {
    isLoading: false,
    userData: null,
    nameTitle: "",
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    pushToken: "",
    notification: {}
  };

  static navigationOptions = ({ navigation }) => {
    return {
      title: navigation.getParam("title", "Edit User")
    };
  };

  componentWillMount() {
    const user = this.props.navigation.getParam("user");
    this.setState({
      userData: user,
      nameTitle: user.name.title,
      firstName: user.name.first,
      lastName: user.name.last,
      email: user.email,
      phone: user.phone
    });
  }

  componentDidMount() {
    if (Platform.OS === "android") {
      this._registerForPushNotificationsAsync();
    }
  }

  render() {
    return (
      <Root>
        <Container>
          <Content padder>
            <Image
              style={styles.image}
              source={{ uri: this.state.userData.picture.large }}
            />
            <Form>
              <Item inlineLabel>
                <Label style={styles.label}>Title</Label>
                <Input
                  style={styles.input}
                  value={this.state.nameTitle}
                  onChangeText={nameTitle => this.setState({ nameTitle })}
                />
              </Item>
              <Item inlineLabel last>
                <Label style={styles.label}>First Name</Label>
                <Input
                  style={styles.input}
                  value={this.state.firstName}
                  onChangeText={firstName => this.setState({ firstName })}
                />
              </Item>
              <Item inlineLabel last>
                <Label style={styles.label}>Last Name</Label>
                <Input
                  style={styles.input}
                  value={this.state.lastName}
                  onChangeText={lastName => this.setState({ lastName })}
                />
              </Item>
              <Item inlineLabel last>
                <Label style={styles.label}>Email</Label>
                <Input
                  style={styles.input}
                  keyboardType="email-address"
                  value={this.state.email}
                  onChangeText={email => this.setState({ email })}
                />
              </Item>
              <Item inlineLabel last>
                <Label style={styles.label}>Phone</Label>
                <Input
                  style={styles.input}
                  keyboardType="phone-pad"
                  value={this.state.phone}
                  onChangeText={phone => this.setState({ phone })}
                />
              </Item>
            </Form>
            <View style={styles.btnContainer}>
              <Button
                bordered
                style={styles.btnCancel}
                onPress={() => this.props.navigation.goBack()}
              >
                <Text>Cancel</Text>
              </Button>
              <Button
                bordered
                style={styles.btnSave}
                onPress={this._actionSave}
              >
                <Text>Save to Firebase</Text>
              </Button>
            </View>
          </Content>
          {this.state.isLoading && <Spinner />}
        </Container>
      </Root>
    );
  }

  async _registerForPushNotificationsAsync() {
    if (Constants.isDevice) {
      const { status: existingStatus } = await Permissions.getAsync(
        Permissions.NOTIFICATIONS
      );
      let finalStatus = existingStatus;
      if (existingStatus !== "granted") {
        const { status } = await Permissions.askAsync(
          Permissions.NOTIFICATIONS
        );
        finalStatus = status;
      }
      if (finalStatus !== "granted") {
        Toast.show({
          text: "Failed to get push token for push notification",
          buttonText: "OK",
          duration: 2000
        });
        return;
      }
      let token = await Notifications.getExpoPushTokenAsync();
      this.setState({ pushToken: token });
    } else {
      Alert.alert(
        "Warning",
        "Push Notifications only shown in physical device"
      );
    }
  };

  async _sendPushNotification() {
    const message = {
      to: this.state.pushToken,
      sound: "default",
      title: "Success",
      body: "User data saved successfully to firebase",
      data: { data: "success" }
    };

    await fetch("https://exp.host/--/api/v2/push/send", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Accept-encoding": "gzip, deflate",
        "Content-Type": "application/json"
      },
      body: JSON.stringify(message)
    });
    this.setState({ isLoading: false });
  };

  _isDataInputValid(dataInputs) {
    for (var i = 0; i < dataInputs.length; i++) {
      if (dataInputs[i].trim() === "") {
        return false;
      }
    }
    return true;
  }

  _actionSave = () => {
    if (
      !this._isDataInputValid([
        this.state.nameTitle,
        this.state.firstName,
        this.state.lastName,
        this.state.email,
        this.state.phone
      ])
    ) {
      Toast.show({
        text: "Data input can't be empty",
        buttonText: "OK",
        duration: 2000
      });
      return;
    }

    Alert.alert(
      "Alert",
      this.state.userData.itemId
        ? "Are you sure wan't to edit this user data ?"
        : "Are you sure wan't to save this user to firebase ?",
      [
        {
          text: "No",
          style: "cancel"
        },
        {
          text: "Yes",
          onPress: () => {
            this.setState({ isLoading: true });
            const userData = {
              picture: {
                thumbnail: this.state.userData.picture.thumbnail,
                large: this.state.userData.picture.large
              },
              name: {
                title: this.state.nameTitle,
                first: this.state.firstName,
                last: this.state.lastName
              },
              email: this.state.email,
              phone: this.state.phone
            };

            this._saveData(userData);
          }
        }
      ],
      { cancelable: true }
    );
  };

  _saveData(userData) {
    if (!this.state.userData.itemId) {
      firebaseApp
        .database()
        .ref("users/")
        .push(userData)
        .then(async () => {
          if (Platform.OS === "android") {
            await this._sendPushNotification();
          } else {
            this.setState({ isLoading: false });
          }
          this.props.navigation.state.params.onBack({ isLoading: true, isFirebase: true });
          this.props.navigation.goBack();
        })
        .catch(error => {
          this.setState({ isLoading: false });
          Toast.show({
            text: error,
            buttonText: "OK",
            duration: 3000
          });
        });
    } else {
      firebaseApp
        .database()
        .ref("/users/" + this.state.userData.itemId)
        .update(userData)
        .then(async () => {
          if (Platform.OS === "android") {
            await this._sendPushNotification();
          } else {
            this.setState({ isLoading: false });
          }
          this.props.navigation.goBack();
        })
        .catch(error => {
          this.setState({ isLoading: false });
          Toast.show({
            text: error,
            buttonText: "OK",
            duration: 3000
          });
        });
    }
  }
}

const styles = StyleSheet.create({
  content: {
    alignItems: "center"
  },
  image: {
    width: 150,
    height: 150,
    borderRadius: 150 / 2,
    alignSelf: "center"
  },
  label: {
    flex: 1
  },
  input: {
    flex: 3
  },
  btnContainer: {
    flexDirection: "row",
    marginTop: 16
  },
  btnCancel: {
    flex: 1,
    marginRight: 5,
    justifyContent: "center"
  },
  btnSave: {
    flex: 1,
    marginLeft: 5,
    justifyContent: "center"
  }
});
