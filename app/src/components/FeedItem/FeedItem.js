/**
 * React Starter Kit (https://www.reactstarterkit.com/)
 *
 * Copyright © 2014-2016 Kriasoft, LLC. All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

import React, {Component, PropTypes} from "react";
import s from "./FeedItem.scss";

class FeedItem extends Component {


  render() {

    return (
      <div className={s.title}>
        <h1>This is an item</h1>
        <p>{this.props.description}</p>
      </div>
    );
  }

}

export default FeedItem;
