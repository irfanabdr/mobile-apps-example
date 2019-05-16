import React from "react";
import { View, ScrollView } from "react-native";
import axios from "axios";

import TrackItem from "../components/TrackItem";
import { Spinner } from "../components/Spinner";

export default class TopTracksScreen extends React.Component {
  state = {
    tracks: [],
    isLoading: false
  };

  static navigationOptions = {
    title: "Top Tracks"
  };

  componentWillMount() {
    this.setState({ isLoading: true });
    axios
      .get(
        "http://ws.audioscrobbler.com/2.0/?method=artist.gettoptracks" +
          "&artist=taylor%20swift" +
          "&api_key=bacbafc15da97fc7a33cd6d9687bdd4a" +
          "&limit=20" +
          "&format=json"
      )
      .then(response => {
        this.setState({
          tracks: response.data.toptracks.track,
          isLoading: false
        });
      });
  }

  _renderTracks() {
    return this.state.tracks.map(track => (
      <TrackItem key={track.name} track={track} />
    ));
  }

  render() {
    if (this.state.isLoading) {
      return <Spinner size="small" />;
    }

    return (
      <View style={{ flex: 1 }}>
        <ScrollView>{this._renderTracks()}</ScrollView>
      </View>
    );
  }
}
