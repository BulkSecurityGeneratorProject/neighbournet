/**
 * React Starter Kit (https://www.reactstarterkit.com/)
 *
 * Copyright © 2014-2016 Kriasoft, LLC. All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

import React, {Component, PropTypes} from "react";
import Slider from "material-ui/Slider";
import autoBind from "react-autobind";

class FilterPane extends Component {

  constructor() {
    super();
    autoBind(this);
  }

  state = {
    slider: 10,
  };

  handleSliderChange = (event, value) => {
    this.setState({slider: value});
    this.props.setRadiusFilter(value);
  };

  render() {
    return (
      <div>
        <Slider
          defaultValue={10}
          value={this.state.slider}
          onChange={this.handleSliderChange.bind(this)}
          min={1}
          max={100}
        />
        <span>{'Chosen radius: '}{this.state.slider}{'km'}</span>
      </div>
    );
  }

}

export default FilterPane;
