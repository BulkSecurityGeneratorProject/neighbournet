/**
 * React Starter Kit (https://www.reactstarterkit.com/)
 *
 * Copyright © 2014-2016 Kriasoft, LLC. All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

import React, {Component, PropTypes} from "react";
import ReactDOM from "react-dom";
import autoBind from "react-autobind";
import IconButton from "material-ui/IconButton";
import FileFileDownload from "material-ui/svg-icons/file/file-download";

class ImageUpload extends Component {

  constructor() {
    super();
    navigator.getMedia = ( navigator.getUserMedia ||
    navigator.webkitGetUserMedia ||
    navigator.mozGetUserMedia ||
    navigator.msGetUserMedia);

    autoBind(this);
    this.state = {
      openMenu: true
    }
  }

  clearphoto() {
    var context = canvas.getContext('2d');
    context.fillStyle = "#AAA";
    context.fillRect(0, 0, canvas.width, canvas.height);

    var data = canvas.toDataURL('image/png');
    photo.setAttribute('src', data);
  }

  takepicture(e) {
    e.preventDefault();
    const canvas = ReactDOM.findDOMNode(this.refs.canvas);
    const video = ReactDOM.findDOMNode(this.refs.video);
    const photo = ReactDOM.findDOMNode(this.refs.photo);

    var context = canvas.getContext('2d');

    context.drawImage(video, 0, 0, 100, 100);

    var data = canvas.toDataURL('image/png');
    photo.setAttribute('src', data);
  }

  componentDidMount() {

    const video = ReactDOM.findDOMNode(this.refs.video);
    navigator.getMedia(
      {
        video: true,
        audio: false
      },
      function (stream) {
        if (navigator.mozGetUserMedia) {
          video.mozSrcObject = stream;
        } else {
          var vendorURL = window.URL || window.webkitURL;
          video.src = vendorURL.createObjectURL(stream);
        }
        video.play();
      },
      function (err) {
        console.log("An error occured! " + err);
      }
    );
  }

  handleOnRequestChange(value, reason) {
    console.log("setting menu state", value);
    this.setState({
      openMenu: value,
    });
  };

  render() {
    const styles = {
      exampleImageInput: {
        cursor: 'pointer',
        position: 'absolute',
        top: 0,
        bottom: 0,
        right: 0,
        left: 0,
        width: '100%',
        opacity: 0,
      },
    };

    let markup = '';
    if (navigator.getMedia) {
      markup =
        <div>
          <div className="camera">
            <video ref="video">Video stream not available.</video>
            <button onClick={this.takepicture}>Take photo</button>
          </div>
          <canvas ref="canvas" width="100" height="100"/>
          <img ref="photo" alt="The screen capture will appear in this box."/>
        </div>
    } else {
      <IconButton><FileFileDownload /></IconButton>
    }
    return (
      <div>
        {markup}
      </div>
      // <IconMenu
      //   iconButtonElement={<IconButton><FileFileDownload /></IconButton>}
      //   open={this.state.openMenu}
      //   onRequestChange={this.handleOnRequestChange.bind(this)}
      // >
      //   <MenuItem value="1" primaryText="Choose Image"/>
      //   <MenuItem value="2" primaryText="Take picture"/>
      // </IconMenu>
    )
  }
}
export default ImageUpload;
