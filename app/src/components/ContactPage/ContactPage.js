/**
 * React Starter Kit (https://www.reactstarterkit.com/)
 *
 * Copyright © 2014-2016 Kriasoft, LLC. All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

import React, {Component, PropTypes} from "react";
import s from "./ContactPage.scss";
import TextField from "material-ui/TextField";
import DatePicker from "material-ui/DatePicker";
import Slider from "material-ui/Slider";
import autoBind from "react-autobind";
import ImageUpload from "../ImageUpload";
import FlatButton from "material-ui/FlatButton";
import {connect} from "react-redux";

const title = 'Add item';

class ContactPageContainer extends Component {

  constructor(props) {
    super(props);

    const minDate = new Date();
    const maxDate = new Date();
    minDate.setFullYear(minDate.getFullYear() - 1);
    minDate.setHours(0, 0, 0, 0);
    maxDate.setFullYear(maxDate.getFullYear() + 1);
    maxDate.setHours(0, 0, 0, 0);

    this.state = {
      minDate: minDate,
      maxDate: maxDate,
      autoOk: false,
      disableYearSelection: false,
    };

    autoBind(this);
  }


  static contextTypes = {
    onSetTitle: PropTypes.func.isRequired,
  };

  componentWillMount() {
    this.context.onSetTitle(title);
  }

  render() {
    return (
      <div className={s.root}>
        <h1>{title}</h1>
        <form action="post">
          <TextField
            hintText="Hint Text"
          />
          <br/>
          <TextField
            hintText="Message Field"
            floatingLabelText="MultiLine and FloatingLabel"
            multiLine={true}
            rows={2}
          />
          <br/>
          <ImageUpload/>
          <DatePicker hintText="Portrait Dialog"/>
          <Slider
            defaultValue={10}
            value={this.state.slider}
            min={1}
            max={100}
          />
          <FlatButton label="Primary"
                      onClick={() => (this.props.addItem("test", this.props.currentLat, this.props.currentLong))}
                      primary={true}/>
        </form>
      </div>
    );
  }

}

const mapStateToProps = (state) => {
  return {
    currentLat: state.location.lat,
    currentLong: state.location.long
  }
};

let nextId = 0;
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    addItem: (text, lat, long) => {
      console.log(ownProps)
      var newVar = {
        type: 'ADD_ITEM',
        id: nextId++,
        text: text,
        lat: lat,
        long: long
      };
      dispatch(newVar)
    }
  }
};

const ContactPage = connect(
  mapStateToProps,
  mapDispatchToProps
)(ContactPageContainer);

export default ContactPage;
