import React from "react";
import { FlatList, View, StyleSheet } from "react-native";
import {
  Container,
  Header,
  Content,
  Title,
  Left,
  Right,
  Button,
  Icon,
  Body,
  Text,
  Thumbnail,
  ListItem
} from "native-base";
import Menu, { MenuItem, MenuDivider } from "react-native-material-menu";
import { createAppContainer, createStackNavigator } from "react-navigation";
import axios from "axios";

import { firebaseApp } from "../../FirebaseConfig";
import { Spinner } from "../components/Spinner";
import EditScreen from "./EditScreen";

class MainScreen extends React.Component {
  state = {
    users: [],
    isLoading: false,
    isFirebase: false
  };

  static navigationOptions = {
    header: null
  };

  _menu = null;
  _setMenuRef = ref => {
    this._menu = ref;
  };

  async componentWillMount() {
    await this.setState({
      isLoading: true,
      isFirebase: this.props.navigation.getParam("firebase", false)
    });

    this._getData();
  }

  render() {
    return (
      <Container>
        <Header>
          <Left />
          <Body>
            <Title>{!this.state.isFirebase ? "Users" : "Firebase Users"}</Title>
          </Body>
          <Right>
            <Menu
              ref={this._setMenuRef}
              button={
                <Button transparent onPress={() => this._menu.show()}>
                  <Icon name="more" />
                </Button>
              }
            >
              <MenuItem onPress={this._actionToMain}>
                {!this.state.isFirebase
                  ? "Show All Data from Firebase"
                  : "Show All Users Data"}
              </MenuItem>
            </Menu>
          </Right>
        </Header>
        <Content contentContainerStyle={{ justifyContent: "center", flex: 1 }}>
          {this._renderContent()}
        </Content>
      </Container>
    );
  }

  _renderContent() {
    if (this.state.isLoading) {
      return <Spinner />;
    }

    if (this.state.users.length === 0) {
      return (
        <Text style={styles.emptyText}>
          No Data
        </Text>
      );
    }

    return (
      <FlatList
        data={this.state.users}
        renderItem={({ item }) => (
          <ListItem avatar onPress={() => this._actionToEdit(item)}>
            <Left>
              <Thumbnail source={{ uri: item.picture.thumbnail }} />
            </Left>
            <Body>
              <Text>
                {item.name.title + " " + item.name.first + " " + item.name.last}
              </Text>
              <Text note>{item.phone}</Text>
            </Body>
            <Right>
              <Button transparent onPress={() => this._actionToEdit(item)}>
                <Icon type="Feather" name="chevron-right" />
              </Button>
            </Right>
          </ListItem>
        )}
        keyExtractor={(item, index) => index.toString()}
      />
    );
  }

  _getData() {
    if (this.state.isFirebase) {
      firebaseApp
        .database()
        .ref("users/")
        .on("value", dataSnapshot => {
          if (dataSnapshot.val()) {
            var userList = [];
            dataSnapshot.forEach(child => {
              userList.push({
                itemId: child.key,
                thumbnail: child.val().thumbnail,
                picture: child.val().picture,
                email: child.val().email,
                phone: child.val().phone,
                name: {
                  title: child.val().name.title,
                  first: child.val().name.first,
                  last: child.val().name.last
                }
              });
            });

            this.setState({
              users: userList,
              isLoading: false
            });
          } else {
            this.setState({
              isLoading: false
            });
          }
        });
    } else {
      axios.get("https://randomuser.me/api/?results=20").then(response => {
        this.setState({
          users: response.data.results,
          isLoading: false
        });
      });
    }
  }

  _actionToEdit(userData) {
    this.props.navigation.navigate("Edit", {
      user: userData,
      title: this.state.isFirebase ? "Edit User" : "Save User",
      onBack: this._onBackAction
    });
  }

  _actionToMain = () => {
    this._menu.hide();
    !this.state.isFirebase
      ? this.props.navigation.replace("Main", { firebase: true })
      : this.props.navigation.replace("Main");
  };

  _onBackAction = data => {
    this.setState(data);
    this._getData();
  };
}

const styles = StyleSheet.create({
  menu: {
    fontWeight: "600"
  },
  emptyText: {
    color: "grey", 
    fontSize: 15, 
    textAlign: "center"
  }
});

export default createAppContainer(
  createStackNavigator(
    {
      Main: MainScreen,
      Edit: EditScreen
    },
    {
      initialRouteName: "Main"
    }
  )
);
