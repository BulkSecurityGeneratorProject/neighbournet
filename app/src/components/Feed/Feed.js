/**
 * React Starter Kit (https://www.reactstarterkit.com/)
 *
 * Copyright © 2014-2016 Kriasoft, LLC. All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

import React, {Component, PropTypes} from "react";
import Item from "../FeedItem";

class Feed extends Component {

  static propTypes = {
    items: PropTypes.array,
  };

  render() {
    var map = this.props.items.map(item => <Item description={item.name} distance={item.distance} key={item.key}/>);
    return (
      <div>
        <span>{'Total results '}{this.props.items.length}</span>
        {map}
      </div>
    );
  }
}

export default Feed;
