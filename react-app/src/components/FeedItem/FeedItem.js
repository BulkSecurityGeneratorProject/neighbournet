/**
 * React Starter Kit (https://www.reactstarterkit.com/)
 *
 * Copyright © 2014-2016 Kriasoft, LLC. All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

import React, {Component, PropTypes} from "react";
import {Card, CardActions, CardHeader, CardMedia, CardTitle, CardText} from "material-ui/Card";

class FeedItem extends Component {


  render() {
    return (
      <Card>
        <CardHeader
          title="URL Avatar"
          subtitle={this.props.distance / 1000 + 'km'}
          avatar="http://placehold.it/75x75"
        />
        <CardMedia
          overlay={<CardTitle title={this.props.text} subtitle="Overlay subtitle"/>}
        >
          <img src="http://placehold.it/1000x500"/>
        </CardMedia>
      </Card>
    );
  }

}

export default FeedItem;
