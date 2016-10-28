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

var items = [
  {
    id: '1',
    name: 'A',
    lat: '',
    long: ''
  },
  {
    id: '2',
    name: 'description 2'
  }
];

class Feed extends Component {

  static propTypes = {
    items: PropTypes.array,
  };

  render() {
    var map = items.map(item => <Item description={item.name}/>);
    return (
      <div>
        {map}
      </div>
    );
  }
}

export default Feed;
