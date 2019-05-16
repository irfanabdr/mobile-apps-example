import React from "react";
import { View, ScrollView } from "react-native";
import axios from "axios";

import AlbumItem from "../components/AlbumItem";
import { Spinner } from "../components/Spinner";

export default class HomeScreen extends React.Component {
  state = {
    albums: [],
    isLoading: false
  };

  static navigationOptions = {
    title: "Albums"
  };

  componentWillMount() {
    this.setState({ isLoading: true });
    axios
      .get("https://rallycoding.herokuapp.com/api/music_albums")
      .then(response => {
        this.setState({
          albums: response.data,
          isLoading: false
        });
      });
  }

  _renderAlbums() {
    return this.state.albums.map(album => (
      <AlbumItem key={album.title} album={album} />
    ));
  }

  render() {
    if (this.state.isLoading) {
      return <Spinner size="small" />;
    }

    return (
      <View style={{ flex: 1 }}>
        <ScrollView>{this._renderAlbums()}</ScrollView>
      </View>
    );
  }
}
