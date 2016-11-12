/**
 * React Starter Kit (https://www.reactstarterkit.com/)
 *
 * Copyright © 2014-2016 Kriasoft, LLC. All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

import React, {Component, PropTypes} from "react";
import cx from "classnames";
import withStyles from "isomorphic-style-loader/lib/withStyles";
import s from "./Navigation.scss";
import IconButton from "material-ui/IconButton";
import ActionHome from "material-ui/svg-icons/content/add-circle-outline";
import Link from "../Link";

class Navigation extends Component {

  static propTypes = {
    className: PropTypes.string,
  };

  render() {
    return (
      <div className={cx(s.root, this.props.className)} role="navigation">
        <Link to="/add">
          <IconButton>
            <ActionHome />
          </IconButton>
        </Link>
        {/*<RaisedButton label="Super Secret Password" />*/}
        {/*<TextBox maxLines={1}/>*/}
        {/*<Link className={s.link} to="/about">Filter</Link>*/}
        {/*<Link className={s.link} to="/add">New</Link>*/}
        {/*<span className={s.spacer}> | </span>*/}
        {/*<Link className={s.link} to="/login">Log in</Link>*/}
        {/*<span className={s.spacer}>or</span>*/}
        {/*<Link className={cx(s.link, s.highlight)} to="/register">Sign up</Link>*/}
      </div>
    );
  }

}

export default withStyles(Navigation, s);
