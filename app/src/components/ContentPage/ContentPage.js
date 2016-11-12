/**
 * React Starter Kit (https://www.reactstarterkit.com/)
 *
 * Copyright © 2014-2016 Kriasoft, LLC. All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

import React, {Component, PropTypes} from "react";
import withStyles from "isomorphic-style-loader/lib/withStyles";
import s from "./ContentPage.scss";
import Feed from "../Feed";
import FilterPane from "../FilterPane";
import autoBind from "react-autobind";
import {connect} from "react-redux";

class ContentPageContainer extends Component {

  constructor(props) {
    super(props);
    this.state = {
      radius: 10,
      lat: 0,
      long: 0
    };
    if (Number.prototype.toRadians === undefined) {
      Number.prototype.toRadians = function () {
        return this * Math.PI / 180;
      };
    }
    autoBind(this);
  }

  static
  propTypes = {
    path: PropTypes.string.isRequired,
    content: PropTypes.string.isRequired,
    title: PropTypes.string,
  };

  static
  contextTypes = {
    onSetTitle: PropTypes.func.isRequired,
  };

  setRadiusFilter(value) {
    this.setState(
      {
        radius: value
      }
    );
  }

  render() {
    var displayedItems = [];
    this.props.feed.forEach(item => {
      //var φ1 = this.state.lat.toRadians(), φ2 = item.lat.toRadians(), Δλ = (item.long - this.state.long).toRadians(), R = 6371e3; // gives d in metres
      //var d = Math.acos(Math.sin(φ1) * Math.sin(φ2) + Math.cos(φ1) * Math.cos(φ2) * Math.cos(Δλ)) * R;
      //if (d <= this.state.radius * 1000) {
      // item.distance = d;
        displayedItems.push(item);
      //}
    }, this);


    this.context.onSetTitle(this.props.title);

    return (
      <div className={s.root}>
        <div className={s.container}>
          <FilterPane setRadiusFilter={this.setRadiusFilter}/>
          <Feed items={displayedItems}/>
          {this.props.path === '/' ? null : <h1>{this.props.title}</h1>}
          <div dangerouslySetInnerHTML={{__html: this.props.content || ''}}/>
        </div>
      </div>
    );
  }

}

const mapStateToProps = (state) => {
  return {
    feed: state.feed
  }
};

const mapDispatchToProps = (dispatch) => {
  return {}
}

const ContentPage = connect(
  mapStateToProps,
  mapDispatchToProps
)(ContentPageContainer);

export default ContentPage;

withStyles(ContentPage, s);
