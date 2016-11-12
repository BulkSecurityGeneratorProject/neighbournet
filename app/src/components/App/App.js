/**
 * React Starter Kit (https://www.reactstarterkit.com/)
 *
 * Copyright © 2014-2016 Kriasoft, LLC. All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

import React, {Component, PropTypes} from "react";
import emptyFunction from "fbjs/lib/emptyFunction";
import s from "./App.scss";
import Header from "../Header";
import Footer from "../Footer";
import {deepOrange500} from "material-ui/styles/colors";
import getMuiTheme from "material-ui/styles/getMuiTheme";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import createLogger from "redux-logger";
import {Provider} from "react-redux";
import {applyMiddleware, createStore} from "redux";
import app from "../../reducers/app";

const muiTheme = getMuiTheme({
  palette: {
    accent1Color: deepOrange500,
  },
});

const logger = createLogger();
let store = createStore(app, applyMiddleware(logger));

class App extends Component {

  static propTypes = {
    context: PropTypes.shape({
      insertCss: PropTypes.func,
      onSetTitle: PropTypes.func,
      onSetMeta: PropTypes.func,
      onPageNotFound: PropTypes.func,
    }),
    children: PropTypes.element.isRequired,
    error: PropTypes.object,
  };

  static childContextTypes = {
    insertCss: PropTypes.func.isRequired,
    onSetTitle: PropTypes.func.isRequired,
    onSetMeta: PropTypes.func.isRequired,
    onPageNotFound: PropTypes.func.isRequired,
  };

  getChildContext() {
    const context = this.props.context;
    return {
      insertCss: context.insertCss || emptyFunction,
      onSetTitle: context.onSetTitle || emptyFunction,
      onSetMeta: context.onSetMeta || emptyFunction,
      onPageNotFound: context.onPageNotFound || emptyFunction,
    };
  }

  componentWillMount() {
    const {insertCss} = this.props.context;
    this.removeCss = insertCss(s);
  }

  componentWillUnmount() {
    this.removeCss();
  }

  render() {
    return !this.props.error ? (
      <Provider store={store}>
        <MuiThemeProvider muiTheme={muiTheme}>
          <div>
            <Header />
            {this.props.children}
            <Footer />
          </div>
        </MuiThemeProvider>
      </Provider>
    ) : this.props.children;
  }

}

export default App;
