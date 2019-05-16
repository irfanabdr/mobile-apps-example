import React from 'react';
import { Text, View, Image, Linking } from 'react-native';
import Card from './Card';
import CardSection from './CardSection';
import Button from './Button';

const TrackItem = ({ track }) => {
  const { name, artist } = track;
  const {
    thumbnailStyle,
    headerContentStyle,
    thumbnailContainerStyle,
    headerTextStyle,
    subTextStyle,
    imageStyle
  } = styles;

  return (
    <Card>
      <CardSection>
        <View style={thumbnailContainerStyle}>
          <Image
            style={thumbnailStyle}
            source={{ uri: "https://i.imgur.com/K3KJ3w4h.jpg" }}
          />
        </View>
        <View style={headerContentStyle}>
          <Text style={headerTextStyle} ellipsizeMode={'tail'} numberOfLines={1}>{name}</Text>
          <Text style={subTextStyle}>{artist.name}</Text>
        </View>
      </CardSection>
    </Card>
  );
};

const styles = {
  headerContentStyle: {
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'center'
  },
  headerTextStyle: {
    fontSize: 15
  },
  subTextStyle: {
    color: 'grey'
  },
  thumbnailStyle: {
    height: 50,
    width: 50
  },
  thumbnailContainerStyle: {
    justifyContent: 'center',
    alignItems: 'center',
    marginLeft: 10,
    marginRight: 10
  },
  imageStyle: {
    height: 300,
    flex: 1,
    width: null
  }
};

export default TrackItem;
